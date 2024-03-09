package com.example.movierama.ui.features.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.common.myutils.addTitleElevationAnimation
import com.example.common.myutils.disableFullScreenTheme
import com.example.common.myutils.hide
import com.example.common.myutils.scrollToUp
import com.example.common.myutils.setLightStatusBars
import com.example.common.myutils.show
import com.example.common.myutils.showUpButtonListener
import com.example.movierama.databinding.FragmentMoviesTypeBinding
import com.example.movierama.model.error_handling.ApiError
import com.example.movierama.ui.utils.addOnLoadMoreListener
import com.example.movierama.ui.utils.showConnectionErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesTypeFragment : Fragment() {

    private val viewModel: MoviesTypeViewModel by viewModels()

    private var _binding: FragmentMoviesTypeBinding? = null
    private val binding get() = _binding!!

    private val args: MoviesTypeFragmentArgs by navArgs()

    private lateinit var moviesAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMoviesTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disableFullScreenTheme()
        setLightStatusBars(true)
        initViews()
    }

    override fun onStart() {
        super.onStart()
        initToolbar()
        initSubscriptions()
        viewModel.fetchMovies(args.movieType)
    }

    private fun initViews() {
        initMoviesListView()

        binding.moveUpBtn.setOnClickListener {
            binding.moviesList.scrollToUp()
        }
        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
        binding.searchIcon.setOnClickListener {
            navigateToSearch()
        }
    }

    private fun initToolbar() {
        binding.toolbar.apply {
            setLeftIconListener { findNavController().popBackStack() }
            setTitle(args.movieType.description)
        }
    }

    private fun initMoviesListView() {
        moviesAdapter = MovieAdapter(onClick = {
            findNavController().navigate(MoviesTypeFragmentDirections.actionNavMoviesToNavMovie(it.id))
        }, onFavouriteClick = {
            viewModel.onFavouriteChanged(it)
        })
        binding.moviesList.apply {
            addTitleElevationAnimation(listOf(binding.toolbar, binding.searchIcon))
            adapter = moviesAdapter
            addTitleElevationAnimation(
                listOf(
                    binding.toolbar,
                )
            )
            addOnLoadMoreListener(loadMoreAction = { viewModel.fetchMore() })
            showUpButtonListener(binding.moveUpBtn)
        }
    }

    private fun hideLoadersAndErrorLayout() {
        binding.moreLoader.hide()
        binding.loader.hide()
        binding.refreshLayout.hide()
        binding.errorLbl.isVisible = false
    }

    private fun initSubscriptions() {
        lifecycle.coroutineScope.launch {
            viewModel.homeState.collect { state ->
                when {
                    state.isLoadingMore -> binding.moreLoader.show()
                    state.isLoading -> {
                        binding.loader.show()
                        moviesAdapter.submitList(emptyList()) // clears the list when show the loader
                    }

                    state.error != null -> doOnError(state.error)
                    else -> doOnFetchData(state)
                }
            }
        }
    }

    private fun doOnFetchData(state: MoviesTypeState) {
        with(state) {
            hideLoadersAndErrorLayout()
            moviesAdapter.submitList(movies)
            handleEmptyData(movies.isEmpty())
        }
    }

    private fun doOnError(error: ApiError) {
        hideLoadersAndErrorLayout()
        showErrorLayout(error)
        if (error == ApiError.NoInternetConnection) showConnectionErrorDialog()
    }

    private fun showErrorLayout(error: ApiError) {
        binding.moviesList.isVisible = false
        binding.errorLbl.apply {
            isVisible = true
            text = getString(error.messageId)
            setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, error.drawableId)
        }
    }

    private fun handleEmptyData(isEmpty: Boolean) {
        binding.moviesList.isVisible = isEmpty.not()
        binding.noResultsLbl.isVisible = isEmpty
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private fun MoviesTypeFragment.navigateToSearch() =
    findNavController().navigate(MoviesTypeFragmentDirections.actionNavMoviesListToNavSearchMovie())

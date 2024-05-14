package com.example.movierama.features.movies.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.example.common.myutils.addTitleElevationAnimation
import com.example.common.myutils.disableFullScreenTheme
import com.example.common.myutils.hide
import com.example.common.myutils.scrollToUp
import com.example.common.myutils.setLightStatusBars
import com.example.common.myutils.show
import com.example.common.myutils.showUpButtonListener
import com.example.movierama.databinding.FragmentMoviesTypeBinding
import com.example.movierama.core.presentation.utils.addOnLoadMoreListener
import com.example.movierama.core.presentation.utils.showConnectionErrorDialog
import com.example.movierama.network.data.ApiError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MoviesTypeFragment : Fragment() {

    private val viewModel: MoviesTypeViewModel by viewModels()

    private var _binding: FragmentMoviesTypeBinding? = null
    private val binding get() = _binding!!

    private val args: MoviesTypeFragmentArgs by navArgs()

    private val movieTypeAdapter by lazy {
        MovieTypeAdapter(onClick = {
            navigateToMovieDetails(it.id)
        }, onFavouriteClick = {
            viewModel.onFavouriteChanged(it)
        })
    }

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
        viewModel.movieType = args.movieType // needs to be initialized before movies flow is initialized
        initToolbar()
        initSubscriptions()
    }

    private fun initViews() {
        initMoviesListView()

        binding.moveUpBtn.setOnClickListener { binding.moviesList.scrollToUp() }
        binding.refreshLayout.setOnRefreshListener { movieTypeAdapter.refresh() }
        binding.searchIcon.setOnClickListener { navigateToSearch() }
    }

    private fun initToolbar() {
        binding.toolbar.apply {
            setLeftIconListener { findNavController().popBackStack() }
            setTitle(args.movieType.description)
        }
    }

    private fun initMoviesListView() {
        binding.moviesList.apply {
            addTitleElevationAnimation(listOf(binding.toolbar, binding.searchIcon))
            adapter =
                movieTypeAdapter.withLoadStateFooter(MoviesTypeLoadStateAdapter { movieTypeAdapter.refresh() })
            showUpButtonListener(binding.moveUpBtn)
        }
        movieTypeAdapter.addLoadStateListener { loadState ->
            when (loadState.source.refresh) {
                LoadState.Loading -> {
                    binding.loader.isVisible = loadState.source.refresh is LoadState.Loading
                            && !binding.refreshLayout.isRefreshing
                }

                is LoadState.Error -> {
                    showErrorLayout((loadState.source.refresh as LoadState.Error).error.message.toString())
                }

                is LoadState.NotLoading -> {
                    hideLoadersAndErrorLayout()
                    handleEmptyData(movieTypeAdapter.itemCount < 1)
                }
            }
        }
    }

    private fun hideLoadersAndErrorLayout() {
        binding.moreLoader.hide()
        binding.loader.hide()
        binding.refreshLayout.hide()
        binding.errorLbl.hide()
    }

    private fun initSubscriptions() {
        lifecycleScope.launch {
            viewModel.movies.collectLatest {
                movieTypeAdapter.submitData(it)
            }
        }
    }

    private fun showErrorLayout(error: String) {
        binding.moviesList.isVisible = false
        binding.errorLbl.apply {
            isVisible = true
            text = error
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

private fun MoviesTypeFragment.navigateToMovieDetails(movieId: Long) =
    findNavController().navigate(MoviesTypeFragmentDirections.actionNavMoviesToNavMovie(movieId))

private fun MoviesTypeFragment.navigateToSearch() =
    findNavController().navigate(MoviesTypeFragmentDirections.actionNavMoviesListToNavSearchMovie())

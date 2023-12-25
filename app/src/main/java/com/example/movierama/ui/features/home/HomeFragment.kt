package com.example.movierama.ui.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lists.MyItemDecoration
import com.example.movierama.R
import com.example.movierama.databinding.FragmentHomeBinding
import com.example.movierama.model.error_handling.ApiError
import com.example.movierama.ui.base.MenuScreen
import com.example.movierama.ui.utils.addOnLoadMoreListener
import com.example.movierama.ui.utils.showConnectionErrorDialog
import com.example.myutils.addTitleElevationAnimation
import com.example.myutils.disableFullScreenTheme
import com.example.myutils.hide
import com.example.myutils.scrollToUp
import com.example.myutils.setLightStatusBars
import com.example.myutils.show
import com.example.myutils.showUpButtonListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var moviesAdapter: MovieAdapter
    private lateinit var moviesTypeAdapter: MoviesTypeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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
    }

    private fun initViews() {
        initMoviesListView()
        initMoviesTypeList()

        binding.moveUpBtn.setOnClickListener {
            binding.moviesList.scrollToUp()
        }
        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
        binding.searchIcon.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_search_movie)
        }
    }

    private fun initMoviesTypeList() {
        moviesTypeAdapter = MoviesTypeAdapter {
            viewModel.onMovieTypeSelected(it)
        }
        binding.moviesTypeList.apply {
            adapter = moviesTypeAdapter
            addItemDecoration(
                MyItemDecoration(
                    spaceSize = resources.getDimensionPixelSize(com.example.myresources.R.dimen.space_small),
                    spanCount = (layoutManager as? GridLayoutManager)?.spanCount ?: 1
                )
            )
        }
    }

    private fun initToolbar() {
        binding.toolbar.apply {
            (requireActivity() as? MenuScreen)?.let { setMenuListener(it.getSideMenu()) }
        }
    }

    private fun initMoviesListView() {
        moviesAdapter = MovieAdapter(onClick = {
            findNavController().navigate(HomeFragmentDirections.actionNavMoviesToNavMovie(it.id))
        }, onFavouriteClick = {
            viewModel.onFavouriteChanged(it)
        })
        binding.moviesList.apply {
            adapter = moviesAdapter
            addTitleElevationAnimation(
                listOf(
                    binding.searchIcon,
                    binding.toolbar,
                    binding.moviesTypeList
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

    private fun doOnFetchData(state: HomeUiState) {
        with(state) {
            hideLoadersAndErrorLayout()
            moviesAdapter.submitList(movies)
            handleEmptyData(movies.isEmpty())
            moviesTypeAdapter.setSelectedType(moviesType)
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

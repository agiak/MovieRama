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
import com.example.movierama.databinding.FragmentHomeBinding
import com.example.movierama.model.MoviesType
import com.example.movierama.ui.base.MenuScreen
import com.example.myutils.addTitleElevationAnimation
import com.example.myutils.disableFullScreenTheme
import com.example.myutils.setLightStatusBars
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val movieTypeAdapter = HomeMovieTypeAdapter(
        onItemClick = { movieId ->
            navigateToMovieDetails(movieId)
        }, onLabelClicked = { movieTypeLabel ->
            navigateToMoviesList(movieTypeLabel)
        }, onFetchingMovies = {
            viewModel.fetchMore(it)
        })

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

    private fun initSubscriptions() {
        lifecycle.coroutineScope.launchWhenStarted {
            viewModel.homeState.collect { state ->
                binding.loader.isVisible = state is HomeState.Loading
                when (state) {
                    is HomeState.FetchingMore -> movieTypeAdapter.submitMoviesByType(
                        movieType = state.moviesType,
                        newMovies = state.movies
                    )

                    is HomeState.Result -> movieTypeAdapter.submitList(state.data)
                    else -> {
                        // Do nothing at the moment
                    }
                }
            }
        }
    }

    private fun initViews() {
        binding.searchIcon.setOnClickListener { navigateToSearch() }
        binding.menuList.addTitleElevationAnimation(listOf(binding.searchIcon, binding.toolbar))
        initList()
    }

    private fun initList() {
        binding.menuList.apply { adapter = movieTypeAdapter }
    }

    private fun initToolbar() {
        binding.toolbar.apply {
            (requireActivity() as? MenuScreen)?.let { setMenuListener(it.getSideMenu()) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private fun HomeFragment.navigateToMovieDetails(movieId: Long) =
    findNavController().navigate(HomeFragmentDirections.actionNavNewMoviesToNavMovie(movieId))

private fun HomeFragment.navigateToMoviesList(moviesType: MoviesType) =
    findNavController().navigate(HomeFragmentDirections.actionNavHomeNewToMoviesList(moviesType))

private fun HomeFragment.navigateToSearch() =
    findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavSearchMovie())
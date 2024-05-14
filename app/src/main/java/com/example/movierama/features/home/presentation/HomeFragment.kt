package com.example.movierama.features.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.common.myutils.addTitleElevationAnimation
import com.example.common.myutils.disableFullScreenTheme
import com.example.common.myutils.hide
import com.example.common.myutils.setLightStatusBars
import com.example.common.myutils.show
import com.example.movierama.core.data.movies.MoviesType
import com.example.movierama.core.data.movies.getHomePosition
import com.example.movierama.core.presentation.base.MenuScreen
import com.example.movierama.databinding.FragmentHomeBinding
import com.example.movierama.features.home.data.HomeState
import com.example.movierama.features.home.presentation.viewholders.HomeViewHolder
import com.example.movierama.features.home.presentation.viewholders.HomeViewHolderActions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val movieTypeAdapter = HomeMovieTypeAdapter(
        actions = HomeViewHolderActions(onItemClick = { movieId ->
            navigateToMovieDetails(movieId)
        }, onLabelClicked = { movieTypeLabel ->
            navigateToMoviesList(movieTypeLabel)
        }, onFetchingMovies = { moviesType ->
            viewModel.fetchMore(moviesType)
        })
    )

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
        lifecycleScope.launch {
            viewModel.homeState.collect { state ->
                binding.loader.isVisible = state is HomeState.Loading
                binding.errorGroup.hide()
                when (state) {
                    is HomeState.FetchingMore -> {
                        val viewHolder = binding.menuList.findViewHolderForLayoutPosition(state.moviesType.getHomePosition()) as? HomeViewHolder<*>
                        viewHolder?.addMovies(state.movies)
                    }

                    is HomeState.Result -> movieTypeAdapter.submitList(state.data)
                    is HomeState.Error -> handleError(state.error.asError(requireContext()))
                    else -> {
                        // Do nothing at the moment
                    }
                }
            }
        }
    }

    private fun handleError(error: String) {
        binding.errorGroup.show()
        binding.errorLabel.text = error
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
    findNavController().navigate(
        HomeFragmentDirections.actionNavNewMoviesToNavMovie(
            movieId
        )
    )

private fun HomeFragment.navigateToMoviesList(moviesType: MoviesType) =
    findNavController().navigate(
        HomeFragmentDirections.actionNavHomeNewToMoviesList(
            moviesType
        )
    )

private fun HomeFragment.navigateToSearch() =
    findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavSearchMovie())

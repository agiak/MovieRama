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
import com.example.movierama.R
import com.example.movierama.databinding.FragmentHomeBinding
import com.example.movierama.model.MoviesType
import com.example.movierama.model.error_handling.ApiError
import com.example.movierama.model.getHomePosition
import com.example.movierama.ui.base.MenuScreen
import com.example.movierama.ui.features.home.viewholders.HomeViewHolder
import com.example.movierama.ui.features.home.viewholders.HomeViewHolderActions
import com.example.myutils.addTitleElevationAnimation
import com.example.myutils.disableFullScreenTheme
import com.example.myutils.setLightStatusBars
import com.example.myutils.showDialog
import dagger.hilt.android.AndroidEntryPoint

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
        lifecycle.coroutineScope.launchWhenStarted {
            viewModel.homeState.collect { state ->
                binding.loader.isVisible = state is HomeState.Loading
                when (state) {
                    is HomeState.FetchingMore -> {
                        val viewHolder = binding.menuList.findViewHolderForLayoutPosition(state.moviesType.getHomePosition()) as? HomeViewHolder<*>
                        viewHolder?.addMovies(state.movies)
                    }

                    is HomeState.Result -> movieTypeAdapter.submitList(state.data)
                    is HomeState.Error -> handleError(state.error)
                    else -> {
                        // Do nothing at the moment
                    }
                }
            }
        }
    }

    private fun handleError(error: ApiError) {
        showDialog(
            context = requireContext(),
            title = error.name,
            message = getString(error.messageId),
            drawableId = error.drawableId,
            isCancelable = true,
            mandatoryButton = getString(R.string.dialog_btn_ok)
        )
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

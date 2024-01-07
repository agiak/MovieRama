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
import com.example.movierama.databinding.FragmentHomeNewBinding
import com.example.movierama.model.MoviesType
import com.example.movierama.ui.base.MenuScreen
import com.example.movierama.ui.utils.addOnLoadMoreListener
import com.example.myutils.addTitleElevation
import com.example.myutils.disableFullScreenTheme
import com.example.myutils.hide
import com.example.myutils.setLightStatusBars
import com.example.myutils.show
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeNewFragment : Fragment() {

    private val viewModel: HomeNewViewModel by viewModels()

    private var _binding: FragmentHomeNewBinding? = null
    private val binding get() = _binding!!

    private lateinit var popularAdapter: HomeMovieAdapter
    private lateinit var nowPlayingAdapter: HomeMovieAdapter
    private lateinit var topRatedAdapter: HomeMovieAdapter
    private lateinit var upcomingAdapter: HomeMovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeNewBinding.inflate(inflater, container, false)
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
                Timber.d("observer was called with state ${state}")
                binding.loader.isVisible = state is HomeState.Loading
                when (state) {
                    is HomeState.MoviesData -> handleAllMovies(state)
                    is HomeState.NowPlayingData -> handleNowPlayingMovies(state)
                    is HomeState.PopularData -> handlePopularMovies(state)
                    is HomeState.TopRatedData -> handleTopRatedMovies(state)
                    is HomeState.UpcomingData -> handleUpcomingMovies(state)
                    else -> {
                        // do nothing at the moment
                    }
                }
            }
        }
    }

    private fun handlePopularMovies(state: HomeState.PopularData) {
        when {
            state.isLoading -> binding.loader.show()
            else -> {
                binding.loader.hide()
                popularAdapter.submitList(state.movies)
            }
        }
    }

    private fun handleNowPlayingMovies(state: HomeState.NowPlayingData) {
        when {
            state.isLoading -> binding.loader.show()
            else -> {
                binding.loader.hide()
                nowPlayingAdapter.submitList(state.movies)
            }
        }
    }

    private fun handleTopRatedMovies(state: HomeState.TopRatedData) {
        when {
            state.isLoading -> binding.loader.show()
            else -> {
                binding.loader.hide()
                topRatedAdapter.submitList(state.movies)
            }
        }
    }

    private fun handleUpcomingMovies(state: HomeState.UpcomingData) {
        when {
            state.isLoading -> binding.loader.show()
            else -> {
                binding.loader.hide()
                upcomingAdapter.submitList(state.movies)
            }
        }
    }

    private fun handleAllMovies(state: HomeState.MoviesData) {
        popularAdapter.submitList(state.popular)
        nowPlayingAdapter.submitList(state.nowPlaying)
        topRatedAdapter.submitList(state.topRated)
        upcomingAdapter.submitList(state.upcoming)
    }

    private fun initViews() {
        binding.searchIcon.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_search_movie)
        }

        binding.nestedScroll.addTitleElevation(listOf(binding.searchIcon, binding.toolbar))

        binding.lblPopular.setOnClickListener { navigateToMoviesListFragment(MoviesType.POPULAR) }
        binding.lblNowPlaying.setOnClickListener { navigateToMoviesListFragment(MoviesType.NOW_PLAYING) }
        binding.lblUpcoming.setOnClickListener { navigateToMoviesListFragment(MoviesType.UPCOMING) }
        binding.lblTopRated.setOnClickListener { navigateToMoviesListFragment(MoviesType.TOP_RATED) }

        initAdapters()
        initLists()
    }

    private fun initAdapters() {
        popularAdapter = produceHomeAdapter()
        upcomingAdapter = produceHomeAdapter()
        topRatedAdapter = produceHomeAdapter()
        nowPlayingAdapter = produceHomeAdapter()
    }

    private fun produceHomeAdapter() = HomeMovieAdapter(
        onClick = { movieId ->
            HomeNewFragmentDirections.actionNavNewMoviesToNavMovie(movieId)
        }
    )

    private fun initLists() {
        binding.listPopular.apply {
            adapter = popularAdapter
            addOnLoadMoreListener(loadMoreAction = {
                Timber.d("fetch more popular was called")
                viewModel.fetchMore(MoviesType.POPULAR)
            })
        }

        binding.listNowPlaying.apply {
            adapter = nowPlayingAdapter
            addOnLoadMoreListener(loadMoreAction = {
                viewModel.fetchMore(MoviesType.NOW_PLAYING)
            })
        }

        binding.listUpcoming.apply {
            adapter = upcomingAdapter
            addOnLoadMoreListener(loadMoreAction = {
                viewModel.fetchMore(MoviesType.UPCOMING)
            })
        }

        binding.listTopRated.apply {
            adapter = topRatedAdapter
            addOnLoadMoreListener(loadMoreAction = {
                viewModel.fetchMore(MoviesType.TOP_RATED)
            })
        }
    }

    private fun initToolbar() {
        binding.toolbar.apply {
            (requireActivity() as? MenuScreen)?.let { setMenuListener(it.getSideMenu()) }
        }
    }

    private fun navigateToMoviesListFragment(type: MoviesType) {
        HomeNewFragmentDirections.actionNavHomeNewToMoviesList(type.description)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
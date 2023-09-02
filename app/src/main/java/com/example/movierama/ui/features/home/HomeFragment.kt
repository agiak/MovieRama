package com.example.movierama.ui.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.lists.MyItemDecoration
import com.example.movierama.databinding.FragmentHomeBinding
import com.example.movierama.domain.error_hadling.getErrorMessageResource
import com.example.movierama.ui.UIState
import com.example.movierama.ui.utils.addOnLoadMoreListener
import com.example.movierama.ui.utils.collectInViewScope
import com.example.myutils.addTitleElevationAnimation
import com.example.myutils.disableFullScreenTheme
import com.example.myutils.hide
import com.example.myutils.scrollToUp
import com.example.myutils.setLightStatusBars
import com.example.myutils.show
import com.example.myutils.showToast
import com.example.myutils.showUpButtonListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: MoviesViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var moviesAdapter: MovieAdapter
    private lateinit var moviesTypeAdapter: MoviesTypeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disableFullScreenTheme()
        setLightStatusBars(true)
        initViews()
        initSubscriptions()
    }

    private fun initViews() {
        initMoviesListView()
        initMoviesTypeList()
        initSearchBar()
        binding.moveUpBtn.setOnClickListener {
            binding.moviesList.scrollToUp()
        }
        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun initMoviesTypeList() {
        moviesTypeAdapter = MoviesTypeAdapter {
            viewModel.onMovieTypeSelected(it)
        }
        binding.moviesTypeList.apply {
            adapter = moviesTypeAdapter
            addItemDecoration(
                MyItemDecoration(resources.getDimensionPixelSize(com.example.myresources.R.dimen.space_small))
            )
        }
    }

    private fun initSearchBar() {
        binding.searchBar.apply {
            doOnTextChanged { text, _, _, _ ->
                binding.clearTextBtn.isVisible = text.isNullOrEmpty().not()
            }
            setActions(
                doBeforeDebounce = {
                    binding.loader.show()
                },
                doAfterDebounce = {
                    viewModel.searchMovies(getSearchValue(it))
                }
            )
        }
        binding.clearTextBtn.setOnClickListener {
            binding.searchBar.text?.clear()
        }
    }

    private fun getSearchValue(input: String): SearchFilter = SearchFilter(movieName = input)

    private fun initMoviesListView() {
        moviesAdapter = MovieAdapter(onClick = {
            findNavController().navigate(HomeFragmentDirections.actionNavMoviesToNavMovie(it.id))
        }, onFavouriteClick = {
            viewModel.onFavouriteChanged(it)
        })
        binding.moviesList.apply {
            adapter = moviesAdapter
            addTitleElevationAnimation(binding.searchBar) // add elevation with scrolling at search bar
            addTitleElevationAnimation(binding.moviesTypeList) // add elevation with scrolling at movies type list
            addOnLoadMoreListener(loadMoreAction = {
                viewModel.fetchMoreMovies()
            })
            showUpButtonListener(binding.moveUpBtn)
        }
    }

    private fun hideLoaders() {
        binding.moreLoader.hide()
        binding.loader.hide()
        binding.refreshLayout.hide()
    }

    private fun initSubscriptions() {
        viewModel.homeState.collectInViewScope(viewLifecycleOwner){ state ->
            when (state) {
                is UIState.Result -> {
                    hideLoaders()
                    Timber.w("Ui updated with ${state.data.size} movies")
                    moviesAdapter.submitList(state.data)
                    handleEmptyData(state.data.isEmpty())
                }

                is UIState.Error -> {
                    hideLoaders()
                    showToast(getString(state.error.getErrorMessageResource()))
                }

                UIState.LoadingMore -> {
                    binding.moreLoader.show()
                }

                UIState.InProgress -> {
                    binding.loader.show()
                }

                else -> {
                    hideLoaders()
                }
            }
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

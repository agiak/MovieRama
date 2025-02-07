package com.example.movierama.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.movierama.databinding.FragmentMoviesBinding
import com.example.movierama.domain.error_hadling.getErrorMessageResource
import com.example.movierama.ui.UIState
import com.example.movierama.ui.utils.addOnLoadMoreListener
import com.example.movierama.ui.utils.addTitleElevationAnimation
import com.example.movierama.ui.utils.hide
import com.example.movierama.ui.utils.scrollToUp
import com.example.movierama.ui.utils.show
import com.example.movierama.ui.utils.showToast
import com.example.movierama.ui.utils.showUpButtonListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private val viewModel: MoviesViewModel by viewModels()

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private lateinit var moviesAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initSubscriptions()
    }

    private fun initViews() {
        initMoviesListView()
        initSearchBar()
        binding.moveUpBtn.setOnClickListener {
            binding.moviesList.scrollToUp()
        }
        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
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

    private fun getSearchValue(input: String): MovieFilter = MovieFilter(movieName = input)

    private fun initMoviesListView() {
        moviesAdapter = MovieAdapter(onClick = {
            findNavController().navigate(MoviesFragmentDirections.actionNavMoviesToNavMovie(it.id))
        }, onFavouriteClick = {
            viewModel.onFavouriteChanged(it)
        })
        binding.moviesList.apply {
            adapter = moviesAdapter
            addTitleElevationAnimation(binding.searchBar)
            addOnLoadMoreListener(loadMoreAction = {
                viewModel.loadMoreMovies()
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeState.collect {
                    when (it) {
                        is UIState.Result -> {
                            hideLoaders()
                            Timber.w("Ui updated with ${it.data.size} movies")
                            moviesAdapter.submitList(it.data)
                            handleEmptyData(it.data.isEmpty())
                        }

                        is UIState.Error -> {
                            hideLoaders()
                            showToast(getString(it.error.getErrorMessageResource()))
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

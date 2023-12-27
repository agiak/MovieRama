package com.example.movierama.ui.features.search_movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.movierama.databinding.FragmentSearchMovieBinding
import com.example.movierama.model.error_handling.ApiError
import com.example.movierama.ui.customviews.DebounceViewActions
import com.example.movierama.ui.utils.addOnLoadMoreListener
import com.example.movierama.ui.utils.showConnectionErrorDialog
import com.example.myutils.addTitleElevationAnimation
import com.example.myutils.disableFullScreenTheme
import com.example.myutils.hide
import com.example.myutils.hideKeyboardOnScroll
import com.example.myutils.setCursorPositionToEnd
import com.example.myutils.setEndDrawable
import com.example.myutils.setLightStatusBars
import com.example.myutils.show
import com.example.myutils.showKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchMovieFragment : Fragment() {

    private var _binding: FragmentSearchMovieBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchMovieViewModel by viewModels()

    private val suggestionAdapter = SearchMovieSuggestionAdapter(onClick = {
        binding.searchBar.apply {
            setText(it.query)
            setCursorPositionToEnd()
        }
    })

    private val movieAdapter = SearchedMoviesAdapter {
        findNavController().navigate(SearchMovieFragmentDirections.actionNavSearchMovieToNavMovie(it.id))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchMovieBinding.inflate(inflater, container, false)
        disableFullScreenTheme()
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
        initSubscriptions()
    }

    private fun initViews() {
        binding.backButton.setOnClickListener { findNavController().popBackStack() }
        initSearchBar()
        initResultsList()
        initSuggestionsList()
    }

    private fun initSuggestionsList() {
        binding.suggestionsList.adapter = suggestionAdapter
    }

    private fun initResultsList() {
        binding.moviesList.apply {
            adapter = movieAdapter
            addTitleElevationAnimation(binding.searchBarLayout)
            addOnLoadMoreListener { viewModel.fetchMore() }
            hideKeyboardOnScroll()
        }
    }

    private fun initSearchBar() {
        binding.searchBar.apply {
            doOnTextChanged { text, _, _, _ ->
                binding.clearTextBtn.isVisible = text.isNullOrBlank().not()
            }
            setActions(object : DebounceViewActions {
                override fun doBeforeDebounce(text: String) {
                    binding.loader.show()
                }

                override fun doAfterDebounce(text: String) {
                    viewModel.onSearchTyped(text)
                }
            })
            requestFocus()
            showKeyboard()
        }
        binding.clearTextBtn.setOnClickListener { binding.searchBar.text?.clear() }
    }

    private fun initSubscriptions() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                handleState(state)
            }
        }
    }

    private fun handleState(state: SearchState) {
        hideLoadersAndErrorLayout()
        when (state) {
            SearchState.Loading -> binding.loader.show()
            SearchState.LoadingMore -> binding.moreLoader.show()
            is SearchState.Error -> doOnError(state.error)
            is SearchState.Result -> {
                binding.suggestionsGroup.isVisible = state.needsToSuggestMovies()
                binding.noResultsLbl.isVisible = state.searchFailed()
                binding.moviesList.isVisible = true
                movieAdapter.submitList(state.movies)
            }

            is SearchState.SuggestionsFetched -> {
                binding.moviesList.hide()
                binding.suggestionsGroup.show()
                suggestionAdapter.submitList(state.suggestions)
            }
        }
    }

    private fun hideLoadersAndErrorLayout() {
        binding.moreLoader.hide()
        binding.loader.hide()
        binding.errorLbl.hide()
        binding.noResultsLbl.hide()
    }

    private fun doOnError(error: ApiError) {
        hideLoadersAndErrorLayout()
        showErrorLayout(error)
        if (error == ApiError.NoInternetConnection) showConnectionErrorDialog()
    }

    private fun showErrorLayout(error: ApiError) {
        binding.moviesList.hide()
        binding.errorLbl.apply {
            show()
            text = getString(error.messageId)
            setEndDrawable(error.drawableId)
        }
    }

    private fun SearchState.Result.searchFailed() = query.isNotEmpty() && movies.isEmpty()
    private fun SearchState.Result.needsToSuggestMovies() = query.isEmpty()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

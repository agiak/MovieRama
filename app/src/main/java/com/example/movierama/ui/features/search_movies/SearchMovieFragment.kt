package com.example.movierama.ui.features.search_movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.movierama.databinding.FragmentSearchMovieBinding
import com.example.movierama.model.error_handling.ApiError
import com.example.movierama.model.search_movie.SearchSuggestion
import com.example.movierama.ui.customviews.DebounceViewActions
import com.example.movierama.ui.utils.addOnLoadMoreListener
import com.example.movierama.ui.utils.showConnectionErrorDialog
import com.example.myutils.disableFullScreenTheme
import com.example.myutils.hide
import com.example.myutils.hideKeyboard
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
        binding.searchBar.setText(it.query)
        //viewModel.onSearchTyped(it.query)
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

        binding.suggestionsList.adapter = suggestionAdapter
    }

    private fun initResultsList() {
        binding.moviesList.apply {
            adapter = movieAdapter
            addOnLoadMoreListener { viewModel.fetchMore() }
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        binding.searchBar.hideKeyboard()
                    }
                }
            })
        }
    }

    private fun initSearchBar() {
        binding.searchBar.apply {
            doOnTextChanged { text, start, before, count ->
                binding.clearTextBtn.isVisible = text.isNullOrBlank().not()
            }
            setActions(object : DebounceViewActions {
                override fun doBeforeDebounce(text: String) {
                    binding.loader.show()
                }

                override fun doAfterDebounce(query: String) {
                    viewModel.onSearchTyped(query)
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
                when {
                    state.isLoadingMore -> binding.moreLoader.show()
                    state.isLoading -> {
                        binding.loader.show()
                        movieAdapter.submitList(emptyList()) // clears the list when show the loader
                    }

                    state.error != null -> doOnError(state.error)
                    else -> doOnFetchData(state)
                }
            }
        }
    }

    private fun doOnFetchData(state: SearchState) {
        hideLoadersAndErrorLayout()
        handleSuggestions(state.suggestions)
        handleMovieResults(state)
    }

    private fun handleMovieResults(state: SearchState) {
        when {
            state.movies.isNotEmpty() && state.query.isNotEmpty() -> {
                movieAdapter.submitList(state.movies)
                binding.moviesList.isVisible = true
                binding.noResultsLbl.isVisible = false
                binding.suggestionsList.isVisible = false
            }

            state.movies.isEmpty() && state.query.isEmpty() -> {
                movieAdapter.submitList(state.movies)
                binding.noResultsLbl.isVisible = false
                binding.suggestionsList.isVisible = false
            }

            state.movies.isEmpty() && state.query.isNotEmpty() -> {
                movieAdapter.submitList(state.movies)
                binding.noResultsLbl.isVisible = true
                binding.suggestionsList.isVisible = false
            }

            state.movies.isEmpty() && state.suggestions.isNotEmpty() -> {
                suggestionAdapter.submitList(state.suggestions)
                binding.suggestionsList.isVisible = true
            }
        }
    }

    private fun handleSuggestions(suggestions: List<SearchSuggestion>) {
        binding.suggestionsList.isVisible = suggestions.isNotEmpty()
        suggestionAdapter.submitList(suggestions)
    }

    private fun doOnError(error: ApiError) {
        hideLoadersAndErrorLayout()
        showErrorLayout(error)
        if (error == ApiError.NoInternetConnection) showConnectionErrorDialog()
    }

    private fun hideLoadersAndErrorLayout() {
        binding.moreLoader.hide()
        binding.loader.hide()
        binding.errorLbl.isVisible = false
    }

    private fun showErrorLayout(error: ApiError) {
        binding.moviesList.isVisible = false
        binding.errorLbl.apply {
            isVisible = true
            text = getString(error.messageId)
            setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, error.drawableId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

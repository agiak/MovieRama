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
                    binding.suggestionsGroup.isVisible = false
                }

                override fun doAfterDebounce(text: String) {
                    viewModel.onSearchTyped(text)
                    binding.suggestionsGroup.isVisible = text.isNotEmpty()
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
                movieAdapter.submitList(state.movies)
            }

            is SearchState.SuggestionsFetched -> {
                binding.suggestionsGroup.isVisible = state.suggestions.isNotEmpty()
                suggestionAdapter.submitList(state.suggestions)
            }
        }
    }

    private fun hideLoadersAndErrorLayout() {
        binding.moreLoader.hide()
        binding.loader.hide()
        binding.errorLbl.isVisible = false
        binding.noResultsLbl.isVisible = false
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

    private fun SearchState.Result.searchFailed() = query.isNotEmpty() && movies.isEmpty()
    private fun SearchState.Result.searchSucceed() = query.isNotEmpty() && movies.isNotEmpty()
    private fun SearchState.Result.needsToSuggestMovies() = query.isEmpty()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

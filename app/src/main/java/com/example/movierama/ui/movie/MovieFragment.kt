package com.example.movierama.ui.movie

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.movierama.data.Movie
import com.example.movierama.databinding.FragmentMovieBinding
import com.example.movierama.ui.PagingScrollingState
import com.example.movierama.ui.UIState
import com.example.movierama.ui.movies.MoviesViewModel
import com.example.movierama.ui.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import gr.baseapps.baselistapp.ui.utils.show
import kotlinx.android.synthetic.main.item_movie.title
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieViewModel by viewModels()

    private val args: MovieFragmentArgs by navArgs()

    private val reviewsAdapter = ReviewAdapter()
    private val similarMovieAdapter = SimilarMovieAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initSubscriptions()
    }

    private fun initViews() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.similarMoviesList.adapter = similarMovieAdapter
        binding.reviewsList.adapter = reviewsAdapter
    }

    private fun initSubscriptions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieState.collect {
                    handleMovieDetails(it.movieDetailsState)
                    handleReviews(it.reviewsState)
                    handleSimilarMovies(it.similarMoviesState)
                }
            }
        }
    }

    private fun handleMovieDetails(movieDetailsState: MovieDetailsState) {
        binding.contentLoader.isVisible = movieDetailsState.isLoading
        if (movieDetailsState.hasError()){
            showToast(movieDetailsState.errorMessage)
            return
        }
        movieDetailsState.movieDetails?.let { movieDetails ->
            with(movieDetails) {
                binding.title.text = title
                binding.type.text = type
                binding.releaseDate.text = releaseDate
                binding.rating.rating = rating
                binding.descriptionField.text = description
                binding.directorField.text = author
                binding.castField.text = cast
            }
        }
    }

    private fun handleSimilarMovies(similarMoviesState: SimilarMoviesState) {
        similarMovieAdapter.submitList(similarMoviesState.similarMovies)
    }

    private fun handleReviews(reviewsState: ReviewsState) {
        reviewsAdapter.submitList(reviewsState.reviews)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.example.movierama.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.movierama.R
import com.example.movierama.data.CreditsDetails
import com.example.movierama.databinding.FragmentMovieBinding
import com.example.movierama.ui.utils.addOnLoadMoreListener
import com.example.movierama.ui.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
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
        viewModel.movieId = args.movieId
        initViews()
        initSubscriptions()
        viewModel.getData()
    }

    private fun initViews() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        with(binding.favouriteBtn) {
            setOnClickListener {
                isSelected = isSelected.not()
                viewModel.onFavouriteChanged()
            }
        }

        initSimilarMoviesViews()
        initReviewsViews()
    }

    private fun initSimilarMoviesViews() {
        binding.similarMoviesList.apply {
            adapter = similarMovieAdapter
            addOnLoadMoreListener {
                viewModel.getMoreSimilarMovies()
            }
        }
    }

    private fun initReviewsViews() {
        binding.reviewsList.apply {
            adapter = reviewsAdapter
            addOnLoadMoreListener {
                viewModel.getMoreReviews()
            }
        }
    }
    private fun initSubscriptions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieState.collect {
                    handleMovieDetails(it.movieDetailsState)
                    handleReviews(it.reviewsState)
                    handleSimilarMovies(it.similarMoviesState)
                    handleMovieCredits(it.creditsDetails)
                }
            }
        }
    }

    private fun handleMovieCredits(creditsDetails: CreditsDetails) {
        with(creditsDetails){
            binding.castField.text = cast
            binding.directorField.text = director
        }
    }

    private fun handleMovieDetails(movieDetailsState: MovieDetailsState) {
        binding.contentLoader.isVisible = movieDetailsState.isLoading
        if (movieDetailsState.hasError()) {
            showToast(movieDetailsState.errorMessage)
            return
        }
        movieDetailsState.movieDetails?.let { movieDetails ->
            with(movieDetails) {
                Glide.with(requireContext())
                    .load(poster)
                    .placeholder(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_movie_placeholder
                        )
                    )
                    .into(binding.logo)
                binding.collapsingLayout.title = title
                binding.type.text = type
                binding.favouriteBtn.isSelected = isFavourite
                binding.releaseDate.text = releaseDate
                binding.rating.rating = rating
                binding.descriptionField.text = description
            }
        }
    }

    private fun handleSimilarMovies(similarMoviesState: SimilarMoviesState) {
        binding.loaderSimilarMovies.isVisible = similarMoviesState.isLoading
        binding.similarMoviesList.isVisible = similarMoviesState.isLoading.not()
        similarMovieAdapter.submitList(similarMoviesState.similarMovies)
    }

    private fun handleReviews(reviewsState: ReviewsState) {
        binding.loaderReviews.isVisible = reviewsState.isLoading
        binding.reviewsList.isVisible = reviewsState.isLoading.not()
        reviewsAdapter.submitList(reviewsState.reviews)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

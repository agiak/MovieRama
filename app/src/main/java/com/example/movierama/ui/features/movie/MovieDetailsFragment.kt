package com.example.movierama.ui.features.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.movierama.R
import com.example.movierama.databinding.FragmentMovieDetailsBinding
import com.example.movierama.domain.useCases.moviedetails.CreditsDetails
import com.example.movierama.domain.useCases.moviedetails.MovieDetailsState
import com.example.movierama.domain.useCases.moviedetails.ReviewsState
import com.example.movierama.domain.useCases.moviedetails.SimilarMoviesState
import com.example.movierama.ui.utils.addOnLoadMoreListener
import com.example.movierama.ui.utils.collectInViewScope
import com.example.myutils.addScrollListener
import com.example.myutils.enableFullScreenTheme
import com.example.myutils.setLightStatusBars
import com.example.myutils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieDetailsViewModel by viewModels()

    private val args: MovieDetailsFragmentArgs by navArgs()

    private val reviewsAdapter = ReviewAdapter()
    private val similarMovieAdapter = SimilarMovieAdapter(
        onClick = { movieId ->
            findNavController().navigate(MovieDetailsFragmentDirections.actionNavMovieSelf(movieId))
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        enableFullScreenTheme()
        setLightStatusBars(true)
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

        binding.appBar.addScrollListener(
            onCollapse = {
                setLightStatusBars(false)
            }, onAlmostExpand = {
                setLightStatusBars(false)
            }, onHalfExpand = {
                setLightStatusBars(true)
            }
        )

        initSimilarMoviesViews()
        initReviewsViews()
    }

    private fun initSimilarMoviesViews() {
        binding.similarMoviesList.apply {
            adapter = similarMovieAdapter
            addOnLoadMoreListener(loadMoreAction = {
                viewModel.getMoreSimilarMovies()
            })
        }
    }

    private fun initReviewsViews() {
        binding.reviewsList.apply {
            adapter = reviewsAdapter
            addOnLoadMoreListener(loadMoreAction = {
                viewModel.getMoreReviews()
            })
        }
    }

    private fun initSubscriptions() {
        viewModel.movieState.collectInViewScope(viewLifecycleOwner) {
            handleMovieDetails(it.movieDetailsState)
            handleReviews(it.reviewsState)
            handleSimilarMovies(it.similarMoviesState)
            handleMovieCredits(it.creditsDetails)
        }
    }

    private fun handleMovieCredits(creditsDetails: CreditsDetails) {
        with(creditsDetails) {
            binding.castField.setFullText(cast)
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
        similarMovieAdapter.submitList(similarMoviesState.similarMovies)
    }

    private fun handleReviews(reviewsState: ReviewsState) {
        if (reviewsState.reviews.isEmpty()) {
            binding.reviewsGroup.isVisible = false
            return
        }
        binding.loaderReviews.isVisible = reviewsState.isLoading
        reviewsAdapter.submitList(reviewsState.reviews)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

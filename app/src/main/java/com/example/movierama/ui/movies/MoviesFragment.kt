package com.example.movierama.ui.movies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movierama.databinding.FragmentMoviesBinding
import com.example.movierama.ui.PagingScrollingState
import com.example.movierama.ui.UIState
import dagger.hilt.android.AndroidEntryPoint
import gr.baseapps.baselistapp.ui.utils.addTitleElevationAnimation
import gr.baseapps.baselistapp.ui.utils.hide
import gr.baseapps.baselistapp.ui.utils.show
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private val viewModel: MoviesViewModel by viewModels()

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private lateinit var moviesAdapter: MovieAdapter

    var pagingState = PagingScrollingState.FETCH_MORE

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
        viewModel.getMoviesPerPage(true)
    }

    private fun initViews(){
        moviesAdapter = MovieAdapter {
            findNavController().navigate(MoviesFragmentDirections.actionNavMoviesToNavMovie(it.id))
        }
        binding.moviesList.apply {
            addTitleElevationAnimation(binding.searchBar)
            val llm = LinearLayoutManager(requireContext()).also {
                it.onRestoreInstanceState(viewModel.layoutManagerState)
            }

            layoutManager = llm
            adapter = moviesAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (pagingState == PagingScrollingState.FETCH_MORE &&
                        llm.findLastCompletelyVisibleItemPosition() != -1 &&
                        llm.findLastCompletelyVisibleItemPosition() >= llm.itemCount - 2
                    ) {
                        binding.moreLoader.show()
                        pagingState = if (viewModel.getMoviesPerPage()) PagingScrollingState.PAUSE
                        else {
                            binding.moreLoader.hide()
                            PagingScrollingState.STOP
                        }
                    }
                }
            })
        }
    }

    private fun hideLoaders(){
        binding.moreLoader.hide()
        binding.loader.hide()
    }

    private fun initSubscriptions(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movies.collect {
                    when (it) {
                        is UIState.Result -> {
                            hideLoaders()
                            Log.d("MoviesFragment","Ui updated with ${it.data.size} movies")
                            moviesAdapter.submitList(it.data)
                            pagingState = PagingScrollingState.FETCH_MORE
                        }
                        is UIState.Error -> {
                            hideLoaders()
                            Toast.makeText(requireContext(),"Error ${it.error.message}",Toast.LENGTH_LONG).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

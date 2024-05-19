package com.example.movierama.features.favourites.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.example.common.myutils.addTitleElevationAnimation
import com.example.common.myutils.disableFullScreenTheme
import com.example.common.myutils.setLightStatusBars
import com.example.movierama.core.presentation.base.MenuScreen
import com.example.movierama.databinding.FragmentFavouritesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavouritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavouritesViewModel by viewModels()

    private lateinit var moviesAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        disableFullScreenTheme()
        setLightStatusBars(true)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initSubscriptions()
        initToolbar()
    }

    private fun initToolbar() {
        binding.toolbar.apply {
            (requireActivity() as? MenuScreen)?.let { setMenuListener(it.getSideMenu()) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initSubscriptions()
    }

    private fun initViews() {
        moviesAdapter = MovieAdapter(onClick = {
            findNavController().navigate(
                FavouritesFragmentDirections.actionNavFavouritesToNavMovie(
                    it.id
                )
            )
        }, onFavouriteClick = {
            viewModel.onFavouriteChanged(it)
        })
        binding.moviesList.apply {
            adapter = moviesAdapter
            addTitleElevationAnimation(binding.toolbar)
        }
    }

    private fun initSubscriptions() {
        lifecycle.coroutineScope.launch {
            viewModel.favouriteMovies.collect {
                moviesAdapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

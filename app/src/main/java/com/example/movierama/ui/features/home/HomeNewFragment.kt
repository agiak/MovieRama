package com.example.movierama.ui.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.example.movierama.R
import com.example.movierama.databinding.FragmentHomeNewBinding
import com.example.movierama.ui.base.MenuScreen
import com.example.movierama.ui.features.movie.SimilarMovieAdapter
import com.example.myutils.disableFullScreenTheme
import com.example.myutils.setLightStatusBars
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeNewFragment: Fragment() {

    private val viewModel: HomeNewViewModel by viewModels()

    private var _binding: FragmentHomeNewBinding? = null
    private val binding get() = _binding!!

    private lateinit var popularAdapter: SimilarMovieAdapter
    private lateinit var nowPlayingAdapter: SimilarMovieAdapter
    private lateinit var topRatedAdapter: SimilarMovieAdapter
    private lateinit var upcomingAdapter: SimilarMovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeNewBinding.inflate(inflater, container, false)
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
        initToolbar()
        initSubscriptions()
    }

    private fun initSubscriptions() {
        lifecycle.coroutineScope.launch {

        }
    }

    private fun initViews() {
        binding.searchIcon.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_search_movie)
        }
    }

    private fun initToolbar() {
        binding.toolbar.apply {
            (requireActivity() as? MenuScreen)?.let { setMenuListener(it.getSideMenu()) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
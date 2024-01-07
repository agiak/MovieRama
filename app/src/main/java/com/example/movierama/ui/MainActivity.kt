package com.example.movierama.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.movierama.R
import com.example.movierama.databinding.ActivityMainBinding
import com.example.movierama.ui.base.MenuScreen
import com.example.movierama.ui.splash.SplashViewModel
import com.example.myutils.addPrintingBackstack
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MenuScreen {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: SplashViewModel by viewModels()
    private lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {

        setUpSplashScreenApi()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setUpNavController()
        setUpSideBar()
    }

    private fun setUpSplashScreenApi() {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.showSplash.value
            }
        }
    }

    private fun setUpNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController
        navController.addPrintingBackstack()
    }

    private fun setUpSideBar() {
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_movies_list,
                R.id.nav_favourites,
                R.id.nav_settings
            ),
            binding.drawerLayout
        )
        findViewById<NavigationView>(R.id.nav_view)
            .setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_activity_main).navigateUp(
            appBarConfiguration
        )
    }

    override fun getSideMenu(): DrawerLayout {
        return binding.drawerLayout
    }
}

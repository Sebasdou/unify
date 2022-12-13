package com.ucc.unify.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.ucc.unify.R
import com.ucc.unify.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigationSetup()
    }

    private fun navigationSetup() {
        binding.bottomNavigationView.background = null
        val navHostFragment = supportFragmentManager
            .findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.topAppBar.setupWithNavController(navController)
        binding.bottomNavigationView.setupWithNavController(navController)
        nav()
    }

    private fun nav() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.topAppBar.visibility = View.GONE
                }
                R.id.blogsFragment -> {
                    binding.topAppBar.navigationIcon =
                        resources.getDrawable(R.drawable.ic_baseline_arrow_back_ios_24, theme)
                    binding.topAppBar.setNavigationOnClickListener { navController.popBackStack() }
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    binding.topAppBar.visibility = View.VISIBLE
                    binding.topAppBar.navigationIcon = null
                }
            }
        }
    }
}
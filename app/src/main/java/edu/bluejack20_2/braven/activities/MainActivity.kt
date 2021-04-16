package edu.bluejack20_2.braven.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.toolbar.setupWithNavController(
            navController, AppBarConfiguration(
                topLevelDestinationIds = setOf(),
                fallbackOnNavigateUpListener = ::onSupportNavigateUp
            )
        )

        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            val isOnLoginFragment = destination.id == R.id.loginFragment
            binding.bottomNavigation.visibility = if (isOnLoginFragment) View.GONE else View.VISIBLE
        }
    }
}
package edu.bluejack20_2.braven.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appSettingPrefs: SharedPreferences? = getSharedPreferences("AppSettingPrefs", 0)
//        val sharedPrefsEdit: SharedPreferences.Editor? = appSettingPrefs?.edit()
        val isNightModeOn: Boolean? = appSettingPrefs?.getBoolean("NightMode", false)

        when(isNightModeOn){
            true -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            false -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }


        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        val navController = navHostFragment.navController

        binding.toolbar.setupWithNavController(
<<<<<<< HEAD
            navController, AppBarConfiguration(navController.graph)
=======
            navController,
            AppBarConfiguration(navController.graph)
>>>>>>> af6a8c077dc9bcdf3fd362b2e5a3db7be169e8aa
        )

        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            val isOnLoginFragment = destination.id == R.id.loginFragment
            binding.bottomNavigation.visibility = if (isOnLoginFragment) View.GONE else View.VISIBLE
        }
    }
}
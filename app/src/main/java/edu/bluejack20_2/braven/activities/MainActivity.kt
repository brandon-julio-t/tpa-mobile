package edu.bluejack20_2.braven.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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

        val appFontPrefs: SharedPreferences? = getSharedPreferences("AppSettingPrefs", 0)
        val isLarge: Boolean? = appFontPrefs?.getBoolean("Large", false)

        when(isLarge){
            true -> {
                var themeID: Int = R.style.FontSizeLarge
                setTheme(themeID)
                Log.wtf("testis", "hehehe")
            }
            else ->{
                var themeID: Int = R.style.FontSizeMedium
                setTheme(themeID)
                Log.wtf("testis", "else")
            }
        }

        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initiateLightOrDarkMode()
        setupToolbarAndBottomNavigationWithNavController()
    }

    private fun initiateLightOrDarkMode() {
        val appSettingPrefs: SharedPreferences? = getSharedPreferences("AppSettingPrefs", 0)
        val isNightModeOn: Boolean? = appSettingPrefs?.getBoolean("NightMode", false)

        when (isNightModeOn) {
            true -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            false -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun setupToolbarAndBottomNavigationWithNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        val navController = navHostFragment.navController

        binding.toolbar.setupWithNavController(
            navController,
            AppBarConfiguration(navController.graph)
        )

        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            val isOnLoginFragment = destination.id == R.id.loginFragment
            binding.bottomNavigation.visibility = if (isOnLoginFragment) View.GONE else View.VISIBLE
        }
    }

    public fun changeFontSize(){

    }
}
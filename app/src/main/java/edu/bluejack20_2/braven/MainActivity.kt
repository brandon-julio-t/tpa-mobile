package edu.bluejack20_2.braven

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack20_2.braven.databinding.ActivityMainBinding
import edu.bluejack20_2.braven.pages.notification.NotificationFragmentDirections
import edu.bluejack20_2.braven.pages.post_detail.PostDetailFragmentDirections
import edu.bluejack20_2.braven.pages.user_profile.UserProfileFragmentDirections
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var authenticationService: AuthenticationService

    override fun onCreate(savedInstanceState: Bundle?) {
        setupPreferredFontSize()
        initiateLightOrDarkMode()

        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbarAndBottomNavigationWithNavController()
    }

    private fun setupPreferredFontSize() {
        val appFontPrefs: SharedPreferences? = getSharedPreferences("AppSettingPrefs", 0)

        when (appFontPrefs?.getBoolean("Large", true)) {
            true -> {
                val themeID: Int = R.style.Theme_BRaVeN_FontLarge
                setTheme(themeID)
            }
            else -> {
                val themeID: Int = R.style.Theme_BRaVeN
                setTheme(themeID)
            }
        }
    }

    private fun initiateLightOrDarkMode() {
        val appSettingPrefs: SharedPreferences? = getSharedPreferences("AppSettingPrefs", 0)

        when (appSettingPrefs?.getBoolean("NightMode", false)) {
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
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.loginFragment,
                R.id.homeFragment,
                R.id.exploreFragment,
                R.id.postCreateFragment,
                R.id.userProfileFragment,
                R.id.aboutFragment
            )
        )

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.setOnMenuItemClickListener {
            it.onNavDestinationSelected(findNavController(R.id.nav_host_fragment))
                    || this.onOptionsItemSelected(it)
        }

        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, _ ->
            val isOnLoginFragment = destination.id == R.id.loginFragment
            binding.bottomNavigation.visibility = if (isOnLoginFragment) View.GONE else View.VISIBLE

            if (destination.id == R.id.accountFragment) {
                controller.navigate(
                    NavGraphDirections.toUserProfile(
                        authenticationService.getUser()?.uid.toString()
                    )
                )
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_logout -> {
            authenticationService.logout(findViewById(R.id.nav_host_fragment))
            true
        }
        else -> false
    }
}
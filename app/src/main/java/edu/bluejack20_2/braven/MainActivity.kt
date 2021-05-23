package edu.bluejack20_2.braven

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack20_2.braven.databinding.ActivityMainBinding
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AndroidNotificationService
import edu.bluejack20_2.braven.services.AuthenticationService
import java.time.LocalDateTime
import java.time.OffsetDateTime
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var authenticationService: AuthenticationService

    @Inject
    lateinit var userService: UserService

    override fun onCreate(savedInstanceState: Bundle?) {
        setupPreferredFontSize()
        initiateLightOrDarkMode()

        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNotification()
        setupToolbarAndBottomNavigationWithNavController()
    }

    private fun setNotification() {
        val intent = Intent(this, AndroidNotificationService::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
            AlarmManager.INTERVAL_FIFTEEN_MINUTES,
            pendingIntent
        )
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
                R.id.aboutFragment,
                R.id.settingFragment
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
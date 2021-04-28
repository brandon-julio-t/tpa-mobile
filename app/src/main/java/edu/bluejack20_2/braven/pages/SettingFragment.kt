package edu.bluejack20_2.braven.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack20_2.braven.databinding.FragmentSettingBinding
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var authenticationService: AuthenticationService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val appSettingPrefs = activity?.getSharedPreferences("AppSettingPrefs", 0)
        val sharedPrefsEdit = appSettingPrefs?.edit()
        val isNightModeOn = appSettingPrefs?.getBoolean("NightMode", false)
        val isLarge = appSettingPrefs?.getBoolean("Large", false)

        when (isNightModeOn) {
            true -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchDark.isChecked = true
            }
            false -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchDark.isChecked = false
            }
        }

        when(isLarge){
            true -> binding.switchSize.isChecked = true
            false -> binding.switchSize.isChecked = false
        }

        binding.switchDark.setOnClickListener{
            when(isNightModeOn){
                true -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    sharedPrefsEdit?.putBoolean("NightMode", false)
                    sharedPrefsEdit?.apply()
                }
                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    sharedPrefsEdit?.putBoolean("NightMode", true)
                    sharedPrefsEdit?.apply()
                }
            }
        }

        binding.switchSize.setOnClickListener{
            sharedPrefsEdit?.putBoolean("Large", !isLarge!!)
            sharedPrefsEdit?.apply()
            requireActivity().recreate()
        }

        binding.buttonLogout.setOnClickListener {
            authenticationService.logout(it)
        }
    }

}
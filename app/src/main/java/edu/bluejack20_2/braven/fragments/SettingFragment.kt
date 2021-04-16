package edu.bluejack20_2.braven.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.databinding.FragmentHomeBinding
import edu.bluejack20_2.braven.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val appSettingPrefs: SharedPreferences? = activity?.getSharedPreferences("AppSettingPrefs", 0)
        val sharedPrefsEdit: SharedPreferences.Editor? = appSettingPrefs?.edit()
        val isNightModeOn: Boolean? = appSettingPrefs?.getBoolean("NightMode", false)

        when(isNightModeOn){
            true -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchDark.isChecked = true
            }
            false -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchDark.isChecked = false
            }
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
    }


}
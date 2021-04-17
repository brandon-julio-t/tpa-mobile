package edu.bluejack20_2.braven.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
//    private var viewText: Array<View>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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


        val appSettingPrefs: SharedPreferences? = activity?.getSharedPreferences(
            "AppSettingPrefs",
            0
        )
        val sharedPrefsEdit: SharedPreferences.Editor? = appSettingPrefs?.edit()
        val isNightModeOn: Boolean? = appSettingPrefs?.getBoolean("NightMode", false)
        val isLarge: Boolean? = appSettingPrefs?.getBoolean("Large", false)

        val viewText = getViewsByTag(binding.root, "textView")

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


//        val isLarge: Boolean


        binding.switchSize.setOnClickListener{

            sharedPrefsEdit?.putBoolean("Large", false)
            sharedPrefsEdit?.apply()
            requireActivity().recreate()


        }



    }


    private fun getViewsByTag(root: ViewGroup, tag: String): ArrayList<View> {
        val views = ArrayList<View>()
        val childCount = root.childCount
        for (i in 0 until childCount) {
            val child = root.getChildAt(i)
            if (child is ViewGroup) {
                views.addAll(getViewsByTag(child, tag)!!)
            }
            val tagObj = child.tag
            if (tagObj != null && tagObj == tag) {
                views.add(child)
            }
        }
        return views
    }



}
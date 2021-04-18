package edu.bluejack20_2.braven.pages.explore

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.databinding.FragmentExploreBinding
import edu.bluejack20_2.braven.databinding.FragmentSettingBinding
import edu.bluejack20_2.braven.domains.explore.ExploreService
import javax.inject.Inject

@AndroidEntryPoint
class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    val binding get() = _binding!!

    @Inject
    lateinit var controller: ExploreController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        controller.bind(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}
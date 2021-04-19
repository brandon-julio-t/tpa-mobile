package edu.bluejack20_2.braven.pages.user_statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack20_2.braven.databinding.FragmentUserStatisticsBinding
import javax.inject.Inject

@AndroidEntryPoint
class UserStatisticsFragment : Fragment() {
    private var _binding: FragmentUserStatisticsBinding? = null

    val binding get() = _binding!!

    @Inject
    lateinit var controller: UserStatisticsController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserStatisticsBinding.inflate(inflater, container, false)
        controller.bind(this)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
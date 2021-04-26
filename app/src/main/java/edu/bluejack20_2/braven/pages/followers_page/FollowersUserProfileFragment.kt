package edu.bluejack20_2.braven.pages.followers_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack20_2.braven.databinding.FragmentFollowersUserProfileBinding
import javax.inject.Inject

@AndroidEntryPoint
class FollowersUserProfileFragment : Fragment() {

    private var _binding: FragmentFollowersUserProfileBinding? = null
    val binding get() = _binding!!
    val userId = arguments?.getString("auth")

    @Inject lateinit var controller: FollowersUserProfileController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowersUserProfileBinding.inflate(inflater, container, false)
        controller.bind(this)
        return binding.root
    }

}
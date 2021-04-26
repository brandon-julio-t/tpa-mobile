package edu.bluejack20_2.braven.pages.following_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack20_2.braven.databinding.FragmentFollowingUserProfileBinding
import javax.inject.Inject

@AndroidEntryPoint
class FollowingUserProfileFragment : Fragment() {

    private var _binding: FragmentFollowingUserProfileBinding? = null
    val binding get() = _binding!!
    val userId = arguments?.getString("auth")

    val args: FollowingUserProfileFragmentArgs by navArgs()

    @Inject
    lateinit var controller: FollowingUserProfileController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingUserProfileBinding.inflate(inflater, container, false)
        controller.bind(this)
        return binding.root
    }


}
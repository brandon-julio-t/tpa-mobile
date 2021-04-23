package edu.bluejack20_2.braven.pages.following_page

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.databinding.FragmentExploreBinding
import edu.bluejack20_2.braven.databinding.FragmentFollowingUserProfileBinding
import edu.bluejack20_2.braven.databinding.FragmentUserProfileBinding
import edu.bluejack20_2.braven.pages.explore.ExploreController
import javax.inject.Inject

@AndroidEntryPoint
class FollowingUserProfileFragment : Fragment() {

    private var _binding: FragmentFollowingUserProfileBinding? = null
    val binding get() = _binding!!
    val userId = arguments?.getString("auth")

    @Inject
    lateinit var controller: FollowingUserProfileController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowingUserProfileBinding.inflate(inflater, container, false)
        controller.bind(this)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
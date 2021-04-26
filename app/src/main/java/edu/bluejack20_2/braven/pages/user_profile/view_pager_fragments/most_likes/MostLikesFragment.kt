package edu.bluejack20_2.braven.pages.user_profile.view_pager_fragments.most_likes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack20_2.braven.databinding.FragmentUserProfileMostLikesBinding
import javax.inject.Inject

@AndroidEntryPoint
class MostLikesFragment(val userId: String? = null) : Fragment() {
    private var _binding: FragmentUserProfileMostLikesBinding? = null

    val binding get() = _binding!!

    @Inject
    lateinit var controller: MostLikesController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileMostLikesBinding.inflate(inflater, container, false)
        controller.bind(this)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
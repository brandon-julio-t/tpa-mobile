package edu.bluejack20_2.braven.pages.notification.view_pager_fragments.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack20_2.braven.databinding.FragmentNotificationFollowingBinding
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFollowingFragment : Fragment() {

    private var _binding: FragmentNotificationFollowingBinding? = null
    val binding get() = _binding!!

    @Inject
    lateinit var controller: NotificationFollowingController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationFollowingBinding.inflate(inflater, container, false)
        controller.bind(this)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
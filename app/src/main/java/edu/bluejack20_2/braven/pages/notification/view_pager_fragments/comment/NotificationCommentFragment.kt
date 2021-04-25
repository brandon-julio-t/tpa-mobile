package edu.bluejack20_2.braven.pages.notification.view_pager_fragments.comment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.databinding.FragmentNotificationCommentBinding
import javax.inject.Inject

@AndroidEntryPoint
class NotificationCommentFragment : Fragment() {

    private var _binding: FragmentNotificationCommentBinding? = null
    val binding get() = _binding!!

    @Inject
    lateinit var controller: NotificationCommentController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNotificationCommentBinding.inflate(inflater, container, false)
        controller.bind(this)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    
}
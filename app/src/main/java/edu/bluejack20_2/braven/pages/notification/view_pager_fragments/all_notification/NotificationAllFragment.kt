package edu.bluejack20_2.braven.pages.notification.view_pager_fragments.all_notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.databinding.FragmentNotificationAllBinding
import javax.inject.Inject

@AndroidEntryPoint
class NotificationAllFragment : Fragment() {

    private var _binding: FragmentNotificationAllBinding? = null
    val binding get() = _binding!!

    @Inject
    lateinit var controller: NotificationAllController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNotificationAllBinding.inflate(inflater, container, false)
        controller.bind(this)
        return binding.root
    }

  
}
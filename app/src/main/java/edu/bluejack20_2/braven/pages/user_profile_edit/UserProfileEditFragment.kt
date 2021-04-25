package edu.bluejack20_2.braven.pages.user_profile_edit

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack20_2.braven.databinding.FragmentUserProfileEditBinding
import javax.inject.Inject

@AndroidEntryPoint
class UserProfileEditFragment : Fragment() {

    private var _binding: FragmentUserProfileEditBinding? = null
    val binding get() = _binding!!
    val viewModel: UserProfileEditViewModel by viewModels()

    @Inject
    lateinit var controller: UserProfileEditController
    lateinit var thumbnailChooserActivityLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        thumbnailChooserActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            it.data?.let { data ->
                controller.onPreviewSelected(data)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileEditBinding.inflate(inflater, container, false)
        controller.bind(this)
        return binding.root
    }
}
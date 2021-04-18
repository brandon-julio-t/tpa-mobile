package edu.bluejack20_2.braven.pages.post_edit

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack20_2.braven.databinding.FragmentPostEditBinding
import javax.inject.Inject

@AndroidEntryPoint
class PostEditFragment : Fragment() {
    private var _binding: FragmentPostEditBinding? = null

    val binding get() = _binding!!
    val viewModel: PostEditViewModel by viewModels()
    val args: PostEditFragmentArgs by navArgs()

    @Inject
    lateinit var controller: PostEditController
    lateinit var thumbnailChooserActivityLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        thumbnailChooserActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                it.data?.let { data ->
                    controller.onThumbnailSelected(data)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostEditBinding.inflate(inflater, container, false)
        controller.bind(this)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package edu.bluejack20_2.braven.pages.create_post

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack20_2.braven.databinding.FragmentCreatePostBinding
import javax.inject.Inject

@AndroidEntryPoint
class CreatePostFragment : Fragment() {
    private var _binding: FragmentCreatePostBinding? = null

    val viewModel: CreatePostViewModel by activityViewModels()
    val binding get() = _binding!!

    @Inject
    lateinit var createPostController: CreatePostController
    lateinit var thumbnailChooserActivityLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        thumbnailChooserActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                it.data?.let { data ->
                    createPostController.onThumbnailSelected(data)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        createPostController.bind(this)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package edu.bluejack20_2.braven.pages.post_create

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
import edu.bluejack20_2.braven.databinding.FragmentCreatePostBinding
import javax.inject.Inject

@AndroidEntryPoint
class PostCreateFragment : Fragment() {
    private var _binding: FragmentCreatePostBinding? = null

    val viewModel: PostCreateViewModel by viewModels()
    val binding get() = _binding!!

    @Inject
    lateinit var createPostController: PostCreateController
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
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createPostController.bind(this)
    }
}
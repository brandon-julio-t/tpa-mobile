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
import edu.bluejack20_2.braven.databinding.FragmentPostCreateBinding
import javax.inject.Inject

@AndroidEntryPoint
class PostCreateFragment : Fragment() {
    private var _binding: FragmentPostCreateBinding? = null

    val binding get() = _binding!!
    val viewModel: PostCreateViewModel by viewModels()

    @Inject
    lateinit var controller: PostCreateController
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
        _binding = FragmentPostCreateBinding.inflate(inflater, container, false)
        controller.bind(this)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
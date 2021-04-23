package edu.bluejack20_2.braven.pages.post_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack20_2.braven.databinding.FragmentPostDetailBinding
import javax.inject.Inject

@AndroidEntryPoint
class PostDetailFragment : Fragment() {
    private var _binding: FragmentPostDetailBinding? = null

    val binding get() = _binding!!
    val args: PostDetailFragmentArgs by navArgs()

    @Inject
    lateinit var controller: PostDetailController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        controller.bind(this)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
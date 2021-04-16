package edu.bluejack20_2.braven.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack20_2.braven.controllers.CreatePostController
import edu.bluejack20_2.braven.databinding.FragmentCreatePostBinding
import javax.inject.Inject

@AndroidEntryPoint
class CreatePostFragment : Fragment() {
    private var _binding: FragmentCreatePostBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var createPostController: CreatePostController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        createPostController.bind(binding, requireActivity())
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
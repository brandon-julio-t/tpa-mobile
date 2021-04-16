package edu.bluejack20_2.braven.controllers

import edu.bluejack20_2.braven.databinding.FragmentCreatePostBinding
import edu.bluejack20_2.braven.services.PostService
import javax.inject.Inject

class CreatePostController @Inject constructor(private val postService: PostService) {
    fun bind(binding: FragmentCreatePostBinding) {
        binding.submit.setOnClickListener {
            val title = binding.title.editText?.text.toString()
            val description = binding.description.editText?.text.toString()
            val category = binding.category.editText?.text.toString()

            postService.createPost(title, description, category).addOnSuccessListener { }
        }
    }
}
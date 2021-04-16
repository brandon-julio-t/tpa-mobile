package edu.bluejack20_2.braven.controllers

import android.view.View
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.databinding.FragmentCreatePostBinding
import edu.bluejack20_2.braven.services.PostService
import javax.inject.Inject

class CreatePostController @Inject constructor(private val postService: PostService) {
    fun bind(binding: FragmentCreatePostBinding, activity: FragmentActivity) {
        binding.submit.setOnClickListener {
            val title = binding.title.editText?.text.toString()
            val description = binding.description.editText?.text.toString()
            val category = binding.category.editText?.text.toString()

            postService.createPost(title, description, category).addOnSuccessListener {
                Snackbar.make(
                    activity.findViewById(R.id.coordinatorLayout),
                    "Post Created",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
}
package edu.bluejack20_2.braven.pages.create_post

import android.content.Intent
import com.google.android.material.snackbar.Snackbar
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.services.ImageMediaService
import edu.bluejack20_2.braven.domains.post.PostService
import javax.inject.Inject

class CreatePostController @Inject constructor(
    private val postService: PostService,
    private val imageMediaService: ImageMediaService
) {
    private lateinit var fragment: CreatePostFragment

    fun bind(fragment: CreatePostFragment) {
        this.fragment = fragment
        fragment.binding.viewModel = fragment.viewModel

        fragment.binding.upload.setOnClickListener {
            val chooserIntent = imageMediaService.createIntent()
            fragment.thumbnailChooserActivityLauncher.launch(chooserIntent)
        }

        fragment.binding.submit.setOnClickListener {
            val title = fragment.viewModel.title.value.toString()
            val description = fragment.viewModel.description.value.toString()
            val category = fragment.viewModel.category.value.toString()
            val thumbnail = fragment.viewModel.thumbnail.value ?: ByteArray(0)

            val (postTask, thumbnailTask) = postService.createPost(
                title,
                description,
                category,
                thumbnail
            )

            postTask.addOnSuccessListener {
                Snackbar.make(
                    fragment.requireActivity().findViewById(R.id.coordinatorLayout),
                    "Post Created",
                    Snackbar.LENGTH_LONG
                ).show()
            }

            thumbnailTask?.addOnSuccessListener {
                Snackbar.make(
                    fragment.requireActivity().findViewById(R.id.coordinatorLayout),
                    "Post Thumbnail Uploaded",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    fun onThumbnailSelected(intent: Intent) {
        intent.data?.let {
            fragment.viewModel.thumbnail.value = imageMediaService.byteArrayFromBitmap(
                imageMediaService.bitmapFromUri(
                    fragment.requireActivity().contentResolver,
                    it
                )
            )
        }
    }
}
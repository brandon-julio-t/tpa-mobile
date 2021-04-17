package edu.bluejack20_2.braven.pages.create_post

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.View
import com.google.android.material.snackbar.Snackbar
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.services.ImageMediaService
import javax.inject.Inject

class CreatePostController @Inject constructor(
    private val postService: PostService,
    private val imageMediaService: ImageMediaService
) {
    private lateinit var fragment: CreatePostFragment
    private var isPostTaskFinished = false
    private var isThumbnailTaskFinished = false

    fun bind(fragment: CreatePostFragment) {
        this.fragment = fragment
        fragment.binding.viewModel = fragment.viewModel

        fragment.viewModel.thumbnail.observe(fragment.viewLifecycleOwner) {
            if (it.isEmpty()) {
                fragment.binding.thumbnailPreview.visibility = View.GONE
                return@observe
            }

            fragment.binding.thumbnailPreview.visibility = View.VISIBLE
            fragment.binding.thumbnailPreview.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    it,
                    0,
                    it.size
                )
            )
        }

        fragment.binding.upload.setOnClickListener {
            val chooserIntent = imageMediaService.createIntent()
            fragment.thumbnailChooserActivityLauncher.launch(chooserIntent)
        }

        fragment.binding.submit.setOnClickListener {
            val title = fragment.viewModel.title.value.toString()
            val description = fragment.viewModel.description.value.toString()
            val category = fragment.viewModel.category.value.toString()
            val thumbnail = fragment.viewModel.thumbnail.value ?: ByteArray(0)

            fragment.binding.progressIndicator.visibility = View.VISIBLE

            val (postTask, thumbnailTask) = postService.createPost(
                title,
                description,
                category,
                thumbnail
            )

            postTask.addOnSuccessListener {
                isPostTaskFinished = true
                bothSaveAndUploadHasFinished()
            }

            thumbnailTask?.addOnSuccessListener {
                isThumbnailTaskFinished = true
                bothSaveAndUploadHasFinished()
            }
        }
    }

    private fun bothSaveAndUploadHasFinished() {
        if (!isPostTaskFinished || !isThumbnailTaskFinished) return

        Snackbar.make(
            fragment.requireActivity().findViewById(R.id.coordinatorLayout),
            "Post Created",
            Snackbar.LENGTH_LONG
        ).show()

        fragment.binding.progressIndicator.visibility = View.INVISIBLE
        fragment.viewModel.reset()
    }

    fun onThumbnailSelected(intent: Intent) {
        intent.data?.let {
            fragment.viewModel.thumbnail.value = imageMediaService.byteArrayFromBitmap(
                imageMediaService.bitmapFromUri(
                    fragment.requireActivity().contentResolver,
                    it
                )
            )

            Snackbar.make(
                fragment.requireActivity().findViewById(R.id.coordinatorLayout),
                "Thumbnail selected",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }
}
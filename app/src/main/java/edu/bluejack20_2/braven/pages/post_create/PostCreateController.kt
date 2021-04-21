package edu.bluejack20_2.braven.pages.post_create

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.View
import com.google.android.material.snackbar.Snackbar
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.domains.post.PostValidator
import edu.bluejack20_2.braven.services.ImageMediaService
import javax.inject.Inject

class PostCreateController @Inject constructor(
    private val postService: PostService,
    private val postValidator: PostValidator,
    private val imageMediaService: ImageMediaService
) {
    private lateinit var fragment: PostCreateFragment

    fun bind(fragment: PostCreateFragment) {
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
            val title = fragment.binding.title.editText?.text.toString()
            val description = fragment.binding.description.editText?.text.toString()
            val category = fragment.binding.category.editText?.text.toString()
            val thumbnail = fragment.viewModel.thumbnail.value ?: ByteArray(0)

            val (message, ok) = postValidator.validate(title, description, category)
            if (!ok) {
                return@setOnClickListener Snackbar.make(
                    fragment.requireActivity().findViewById(R.id.coordinatorLayout),
                    message,
                    Snackbar.LENGTH_LONG
                ).show()
            }

            fragment.binding.progressIndicator.visibility = View.VISIBLE

            postService.createPost(
                title,
                description,
                category,
                thumbnail
            ).addOnSuccessListener {
                Snackbar.make(
                    fragment.requireActivity().findViewById(R.id.coordinatorLayout),
                    "Post created",
                    Snackbar.LENGTH_LONG
                ).show()

                fragment.binding.progressIndicator.visibility = View.INVISIBLE
                fragment.viewModel.reset()
                fragment.binding.title.editText?.setText("")
                fragment.binding.description.editText?.setText("")
                fragment.binding.category.editText?.setText("")
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

            Snackbar.make(
                fragment.requireActivity().findViewById(R.id.coordinatorLayout),
                "Thumbnail selected",
                Snackbar.LENGTH_LONG
            ).show()
        }

    }
}
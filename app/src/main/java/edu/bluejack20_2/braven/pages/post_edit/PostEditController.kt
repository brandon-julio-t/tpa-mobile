package edu.bluejack20_2.braven.pages.post_edit

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.View
import com.google.android.material.snackbar.Snackbar
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.domains.post.PostValidator
import edu.bluejack20_2.braven.services.ImageMediaService
import javax.inject.Inject

class PostEditController @Inject constructor(
    private val postService: PostService,
    private val postValidator: PostValidator,
    private val imageMediaService: ImageMediaService
) {
    private lateinit var fragment: PostEditFragment

    fun bind(fragment: PostEditFragment) {
        this.fragment = fragment

        val binding = fragment.binding
        val viewModel = fragment.viewModel

        postService.getPostById(fragment.args.postId).get().addOnSuccessListener { query ->
            query.data?.let { post ->
                binding.title.editText?.setText(post["title"].toString())
                binding.description.editText?.setText(post["description"].toString())
                binding.category.editText?.setText(post["category"].toString())
                postService.getStorageReference(post["thumbnailId"].toString())
                    .getBytes(Long.MAX_VALUE)
                    .addOnSuccessListener { image ->
                        viewModel.thumbnail.value = image
                    }

                viewModel.thumbnail.observe(fragment.viewLifecycleOwner) {
                    if (it.isEmpty()) {
                        binding.thumbnailPreview.visibility = View.GONE
                        return@observe
                    }

                    binding.thumbnailPreview.visibility = View.VISIBLE
                    binding.thumbnailPreview.setImageBitmap(
                        BitmapFactory.decodeByteArray(
                            it,
                            0,
                            it.size
                        )
                    )
                }

                binding.upload.setOnClickListener {
                    val chooserIntent = imageMediaService.createIntent()
                    fragment.thumbnailChooserActivityLauncher.launch(chooserIntent)
                }

                binding.submit.setOnClickListener {
                    val title = binding.title.editText?.text.toString()
                    val description = binding.description.editText?.text.toString()
                    val category = binding.category.editText?.text.toString()
                    val thumbnail = viewModel.thumbnail.value ?: ByteArray(0)

                    val (message, ok) = postValidator.validate(title, description, category)
                    if (!ok) {
                        return@setOnClickListener Snackbar.make(
                            fragment.requireActivity().findViewById(R.id.coordinatorLayout),
                            message,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    binding.progressIndicator.visibility = View.VISIBLE

                    postService.updatePost(
                        fragment.args.postId,
                        title,
                        description,
                        category,
                        thumbnail,
                        post["thumbnailId"].toString()
                    ).addOnSuccessListener {
                        Snackbar.make(
                            fragment.requireActivity().findViewById(R.id.coordinatorLayout),
                            "Post Updated",
                            Snackbar.LENGTH_LONG
                        ).show()

                        binding.progressIndicator.visibility = View.INVISIBLE
                    }
                }
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
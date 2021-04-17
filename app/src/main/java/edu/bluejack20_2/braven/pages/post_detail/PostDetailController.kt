package edu.bluejack20_2.braven.pages.post_detail

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.domains.comment.CommentFirestorePagingAdapter
import edu.bluejack20_2.braven.domains.comment.CommentService
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.factories.FirestorePagingAdapterOptionFactory
import edu.bluejack20_2.braven.modules.GlideApp
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

class PostDetailController @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val postService: PostService,
    private val commentService: CommentService
) {
    fun bind(fragment: PostDetailFragment) {
        val binding = fragment.binding
        val post = fragment.args.post.get("post") as HashMap<*, *>
        val user = authenticationService.getUser()
        val username = user?.displayName.toString()
        val createdAt = (post["timestamp"] as Timestamp).toDate().toString()

        authenticationService.getUserById(post["userId"].toString()).addOnSuccessListener {
            if (it["photoUrl"] != null) {
                Glide.with(fragment)
                    .load(it["photoUrl"].toString())
                    .into(binding.posterProfilePicture)
            }
        }

        binding.posterInformation.text = fragment.getString(
            R.string.poster_information,
            username,
            createdAt
        )

        binding.title.text = post["title"].toString()
        binding.createdAt.text = createdAt
        binding.category.text = post["category"].toString()
        binding.description.text = post["description"].toString()

        val storageReference = postService.getStorageReference(post["id"].toString())
        GlideApp.with(fragment)
            .load(storageReference)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.thumbnail.layoutParams.height = 0
                    binding.thumbnail.requestLayout()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .into(binding.thumbnail)

        binding.commentAs.text = fragment.getString(
            R.string.comment_as,
            username
        )

        binding.postComment.setOnClickListener {
            val comment = binding.comment.text.toString()

            commentService.createComment(post, user, comment).addOnSuccessListener {
                Snackbar.make(
                    fragment.requireActivity().findViewById(R.id.coordinatorLayout),
                    "Comment posted",
                    Snackbar.LENGTH_LONG
                ).show()

                binding.comment.setText("")
            }
        }

        binding.comments.layoutManager =
            object : LinearLayoutManager(fragment.requireActivity(), VERTICAL, false) {
                override fun canScrollVertically(): Boolean = false
            }

        binding.comments.adapter = CommentFirestorePagingAdapter(
            fragment,
            authenticationService,
            FirestorePagingAdapterOptionFactory(
                fragment,
                commentService.getAllCommentsByPost(post)
            ).create()
        )
    }
}
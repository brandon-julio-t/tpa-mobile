package edu.bluejack20_2.braven.pages.post_detail

import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.databinding.FragmentPostDetailBinding
import edu.bluejack20_2.braven.domains.comment.CommentFirestorePagingAdapter
import edu.bluejack20_2.braven.domains.comment.CommentService
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.factories.FailedRequestListenerFactory
import edu.bluejack20_2.braven.factories.FirestorePagingAdapterOptionsFactory
import edu.bluejack20_2.braven.modules.GlideApp
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

class PostDetailController @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val userService: UserService,
    private val postService: PostService,
    private val commentService: CommentService
) {
    fun bind(fragment: PostDetailFragment) {
        val binding = fragment.binding

        val query = postService.getPostById(fragment.args.postId)

        query.addSnapshotListener { it, _ ->
                binding.like.text = fragment.getString(
                    R.string.like,
                    it?.getLong("likersCount") ?: 0
                )
                binding.dislike.text = fragment.getString(
                    R.string.dislike,
                    it?.getLong("dislikersCount") ?: 0
                )
            }

        query.get().addOnSuccessListener {
            bindEvents(binding, fragment, it)
            bindUIs(binding, fragment, it)
        }
    }

    private fun bindEvents(
        binding: FragmentPostDetailBinding,
        fragment: PostDetailFragment,
        post: DocumentSnapshot
    ) {
        listOf(binding.posterProfilePicture, binding.posterInformation).forEach {
            it.setOnClickListener {
                fragment.findNavController()
                    .navigate(
                        PostDetailFragmentDirections.postDetailToUserProfile(
                            post["userId"].toString()
                        )
                    )
            }
        }
    }

    private fun bindUIs(
        binding: FragmentPostDetailBinding,
        fragment: PostDetailFragment,
        post: DocumentSnapshot
    ) {
        val user = authenticationService.getUser()
        val username = user?.displayName.toString()
        val createdAt = (post["timestamp"] as? Timestamp)?.toDate().toString()
        val commentsAdapter = CommentFirestorePagingAdapter(
            fragment,
            userService,
            FirestorePagingAdapterOptionsFactory(
                fragment,
                commentService.getAllCommentsByPost(post)
            ).create()
        )

        userService.getUserById(post["userId"].toString()).get().addOnSuccessListener {
            binding.posterInformation.text = fragment.getString(
                R.string.poster_information,
                it["displayName"],
                createdAt
            )

            if (it["photoUrl"] != null) {
                Glide.with(fragment)
                    .load(it["photoUrl"].toString())
                    .into(binding.posterProfilePicture)
            }
        }

        if (authenticationService.getUser()?.uid == post["userId"]) {
            binding.editPost.visibility = View.VISIBLE
            binding.editPost.setOnClickListener {
                fragment.findNavController()
                    .navigate(PostDetailFragmentDirections.postDetailToPostEdit(post["id"].toString()))
            }
        }

        binding.title.text = post["title"].toString()
        binding.createdAt.text = createdAt
        binding.category.text = post["category"].toString()
        binding.description.text = post["description"].toString()

        GlideApp.with(fragment)
            .load(postService.getStorageReference(post["thumbnailId"].toString()))
            .listener(
                FailedRequestListenerFactory {
                    binding.thumbnail.layoutParams.height = 0
                    binding.thumbnail.requestLayout()
                }.create()
            )
            .into(binding.thumbnail)

        binding.like.setOnClickListener {
            postService.likePost(post).addOnSuccessListener {
                Snackbar.make(
                    fragment.requireActivity().findViewById(R.id.coordinatorLayout),
                    "Post liked",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        binding.dislike.setOnClickListener {
            postService.dislikePost(post).addOnSuccessListener {
                Snackbar.make(
                    fragment.requireActivity().findViewById(R.id.coordinatorLayout),
                    "Post disliked",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

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
                commentsAdapter.refresh()
            }
        }

        binding.comments.layoutManager =
            object : LinearLayoutManager(fragment.requireActivity(), VERTICAL, false) {
                override fun canScrollVertically(): Boolean = false
            }

        binding.comments.adapter = commentsAdapter
    }
}
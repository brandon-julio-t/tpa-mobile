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
import edu.bluejack20_2.braven.domains.comment.CommentFirestorePagingAdapterModule
import edu.bluejack20_2.braven.domains.comment.CommentService
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.factories.FailedRequestListenerFactory
import edu.bluejack20_2.braven.modules.GlideApp
import edu.bluejack20_2.braven.services.AuthenticationService
import edu.bluejack20_2.braven.services.TimestampService
import javax.inject.Inject

class PostDetailController @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val userService: UserService,
    private val postService: PostService,
    private val commentService: CommentService,
    private val timestampService: TimestampService,
    private val commentFirestorePagingAdapterModule: CommentFirestorePagingAdapterModule,
    private val notificationService: NotificationService
) {
    fun bind(fragment: PostDetailFragment) {
        val binding = fragment.binding

        val query = postService.getPostById(fragment.args.postId)

        query.get().addOnSuccessListener {
            bindEvents(binding, fragment, it)
            bindUIs(binding, fragment, it)
        }

        query.addSnapshotListener { post, _ ->
            post?.let {
                updateButtonsIcon(binding, it)
                updateButtonsText(binding, it)
                updateButtonsEventListener(binding, post)
            }
        }
    }

    private fun updateButtonsEventListener(
        binding: FragmentPostDetailBinding,
        post: DocumentSnapshot
    ) {
        onLike(binding, post)
        onDislike(binding, post)
    }

    private fun onLike(
        binding: FragmentPostDetailBinding,
        post: DocumentSnapshot
    ) {
        binding.like.setOnClickListener {
            val likers = (post.get("likers") as? List<*>)
                ?.mapNotNull { it as? String }
                ?: emptyList()

            authenticationService.getUser()?.let { auth ->
                val isLiked = likers.contains(auth.uid)
                if (isLiked) {
                    postService.unlikeAndDislikePost(post)
                    notificationService.deleteNotificationLike(
                        auth,
                        post.get("userId").toString(),
                        post.id
                    )

                } else {
                    postService.likePost(post)
                    notificationService.deleteNotificationDislike(
                        auth,
                        post["userId"].toString(),
                        post.id
                    )
                    notificationService.addNotificationLike(
                        auth,
                        post["userId"].toString(),
                        post.id
                    )
                }
            }
        }
    }

    private fun onDislike(
        binding: FragmentPostDetailBinding,
        post: DocumentSnapshot
    ) {
        binding.dislike.setOnClickListener {
            val dislikers = (post.get("dislikers") as? List<*>)
                ?.mapNotNull { it as? String }
                ?: emptyList()

            authenticationService.getUser()?.let { auth ->
                val isDisliked = dislikers.contains(auth.uid)

                if (isDisliked) {
                    postService.unlikeAndDislikePost(post)
                    notificationService.deleteNotificationDislike(
                        auth,
                        post["userId"].toString(),
                        post.id
                    )
                } else {
                    postService.dislikePost(post)
                    notificationService.deleteNotificationLike(
                        auth,
                        post["userId"].toString(),
                        post.id
                    )
                    notificationService.addNotificationDislike(
                        auth,
                        post["userId"].toString(),
                        post.id
                    )
                }
            }
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
        val createdAt = (post["timestamp"] as? Timestamp)?.let { timestampService.prettyTime(it) }
        val commentsAdapter = commentFirestorePagingAdapterModule.Adapter(
            fragment,
            commentService.getAllCommentsByPost(post)
        )

        userService.getUserById(post["userId"].toString()).get().addOnSuccessListener {
            binding.posterInformation.text = fragment.getString(
                R.string.poster_information,
                it["displayName"],
                createdAt
            )

            if(it!!.get("photoUrl") == null){
                Glide.with(binding.root)
                    .load(R.drawable.ic_baseline_account_circle_24)
                    .into(binding.posterProfilePicture)
            }
            else{
                it!!.get("photoUrl")?.let { url ->
                    Glide.with(binding.root)
                        .load(url.toString())
                        .into(binding.posterProfilePicture)
                }
            }

        }

        if (authenticationService.getUser()?.uid == post["userId"]) {
            binding.editPost.visibility = View.VISIBLE
            binding.editPost.setOnClickListener {
                fragment.findNavController()
                    .navigate(PostDetailFragmentDirections.postDetailToPostEdit(post.id))
            }
        }

        binding.title.text = post["title"].toString()
//        binding.createdAt.text = createdAt
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

        binding.commentAs.text = fragment.getString(
            R.string.comment_as,
            username
        )

        Glide.with(fragment)
            .load(user?.photoUrl.toString())
            .into(binding.posterProfilePictureComment)

        binding.postComment.setOnClickListener {
            val comment = binding.comment.text.toString()

            commentService.createComment(post, user, comment).addOnSuccessListener {
                Snackbar.make(
                    fragment.requireActivity().findViewById(R.id.coordinatorLayout),
                    fragment.getString(R.string.sb_comment_posted),
                    Snackbar.LENGTH_LONG
                ).show()

                binding.comment.setText("")
                commentsAdapter.refresh()
            }
        }

        binding.comments.layoutManager =
            object : LinearLayoutManager(fragment.requireActivity()) {
                override fun canScrollVertically(): Boolean = false
            }

        binding.comments.adapter = commentsAdapter
    }

    private fun updateButtonsIcon(binding: FragmentPostDetailBinding, post: DocumentSnapshot) {
        authenticationService.getUser()?.let { user ->
            val likers = (post.get("likers") as? List<*>)
                ?.mapNotNull { it as? String }
                ?: emptyList()

            val dislikers = (post.get("dislikers") as? List<*>)
                ?.mapNotNull { it as? String }
                ?: emptyList()

            val isLiked = likers.contains(user.uid)
            val isDisliked = dislikers.contains(user.uid)

            if (isLiked) {
                binding.likeDislikeButtonsGroup.check(binding.like.id)
            } else if (isDisliked) {
                binding.likeDislikeButtonsGroup.check(binding.dislike.id)
            }
        }
    }

    private fun updateButtonsText(
        binding: FragmentPostDetailBinding,
        post: DocumentSnapshot
    ) {
        binding.like.text = (post.getLong("likersCount") ?: 0).toString()
        binding.dislike.text = (post.getLong("dislikersCount") ?: 0).toString()
    }
}
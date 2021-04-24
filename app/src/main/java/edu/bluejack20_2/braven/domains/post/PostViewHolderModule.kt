package edu.bluejack20_2.braven.domains.post

import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.NavGraphDirections
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.databinding.ItemPostBinding
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.modules.GlideApp
import edu.bluejack20_2.braven.services.AuthenticationService
import edu.bluejack20_2.braven.services.TimestampService
import javax.inject.Inject

class PostViewHolderModule @Inject constructor(
    private val userService: UserService,
    private val postService: PostService,
    private val authenticationService: AuthenticationService,
    private val notificationService: NotificationService,
    private val timestampService: TimestampService
) {
    inner class ViewHolder(
        private val binding: ItemPostBinding,
        private val fragment: Fragment,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: DocumentSnapshot) {
            binding.title.text = post["title"].toString()
            binding.category.text = post["category"].toString()
            binding.createdAt.text = (post["timestamp"] as? Timestamp)?.let {
                timestampService.prettyTime(it)
            }

            binding.cardLayout.setOnClickListener {
                fragment.findNavController().navigate(
                    NavGraphDirections.toPostDetail(post.id)
                )
            }

            userService.getUserById(post["userId"].toString()).get().addOnSuccessListener { user ->
                binding.posterDisplayName.text = user["displayName"].toString()

                user["photoUrl"]?.let { url ->
                    Glide.with(binding.root)
                        .load(url.toString())
                        .into(binding.posterProfilePicture)
                }
            }

            val storageReference = postService.getStorageReference(post["thumbnailId"].toString())
            GlideApp.with(binding.root).load(storageReference).into(binding.thumbnail)

            post.reference.addSnapshotListener { updatedPost, _ ->
                updatedPost?.let {
                    updateButtonsIcon(it)
                    updateButtonsText(it, post)
                }
            }
        }

        private fun updateButtonsIcon(post: DocumentSnapshot) {
            authenticationService.getUser()?.let { user ->
                val likers = (post.get("likers") as? List<*>)
                    ?.mapNotNull { it as? String }
                    ?: emptyList()

                val dislikers = (post.get("dislikers") as? List<*>)
                    ?.mapNotNull { it as? String }
                    ?: emptyList()

                val isLiked = likers.contains(user.uid)
                val isDisliked = dislikers.contains(user.uid)

                binding.like.visibility = if (isLiked) View.VISIBLE else View.GONE
                binding.unlike.visibility = if (!isLiked) View.VISIBLE else View.GONE

                binding.dislike.visibility = if (isDisliked) View.VISIBLE else View.GONE
                binding.undislike.visibility = if (!isDisliked) View.VISIBLE else View.GONE
            }
        }

        private fun updateButtonsText(
            updatedPost: DocumentSnapshot,
            post: DocumentSnapshot
        ) {
            listOf(binding.like, binding.unlike).forEach {
                it.text = fragment.getString(
                    R.string.like,
                    updatedPost.getLong("likersCount") ?: 0
                )

                it.setOnClickListener { onLike(updatedPost, post) }
            }

            listOf(binding.dislike, binding.undislike).forEach {
                it.text = fragment.getString(
                    R.string.dislike,
                    updatedPost.getLong("dislikersCount") ?: 0
                )

                it.setOnClickListener { onDislike(updatedPost, post) }
            }
        }

        private fun onLike(updatedPost: DocumentSnapshot?, post: DocumentSnapshot) {
            val likers = (updatedPost?.get("likers") as? List<*>)
                ?.mapNotNull { it as? String }
                ?: emptyList()

            authenticationService.getUser()?.let { auth ->
                val isLiked = likers.contains(auth.uid)

                val action =
                    if (isLiked) postService.unlikeAndDislikePost(post)
                    else postService.likePost(post)

                action.addOnSuccessListener {
                    notificationService.deleteNotificationDislike(
                        authenticationService.getUser(),
                        post["userId"].toString(),
                        post["id"].toString()
                    )

                    notificationService.addNotificationLike(
                        authenticationService.getUser(),
                        post["userId"].toString(),
                        post["id"].toString(),
                    )
                }
            }
        }

        private fun onDislike(updatedPost: DocumentSnapshot?, post: DocumentSnapshot) {
            val dislikers = (updatedPost?.get("dislikers") as? List<*>)
                ?.mapNotNull { it as? String }
                ?: emptyList()

            authenticationService.getUser()?.let { auth ->
                val isDisliked = dislikers.contains(auth.uid)

                val action =
                    if (isDisliked) postService.unlikeAndDislikePost(post)
                    else postService.dislikePost(post)

                action.addOnSuccessListener {
                    notificationService.deleteNotificationLike(
                        authenticationService.getUser(),
                        post["userId"].toString(),
                        post["id"].toString()
                    )

                    notificationService.addNotificationDislike(
                        authenticationService.getUser(),
                        post["userId"].toString(),
                        post["id"].toString()
                    )
                }
            }
        }
    }
}
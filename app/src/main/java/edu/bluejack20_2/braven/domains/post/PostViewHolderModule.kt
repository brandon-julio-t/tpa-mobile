package edu.bluejack20_2.braven.domains.post

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.NavGraphDirections
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
                    updateButtonsIcon(updatedPost)
                    updateButtonsText(updatedPost)

                    binding.like.setOnClickListener { onLike(updatedPost, post) }
                    binding.dislike.setOnClickListener { onDislike(updatedPost, post) }
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

                if (isLiked) {
                    binding.likeDislikeButtonsGroup.check(binding.like.id)
                } else if (isDisliked) {
                    binding.likeDislikeButtonsGroup.check(binding.dislike.id)
                }
            }
        }

        private fun updateButtonsText(post: DocumentSnapshot) {
            binding.like.text = (post.getLong("likersCount") ?: 0).toString()
            binding.dislike.text = (post.getLong("dislikersCount") ?: 0).toString()
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
                    when(isLiked){
                        true -> {
                            notificationService.deleteNotificationLike(
                                authenticationService.getUser(),
                                post["userId"].toString(),
                                post.id
                            )
                        }
                        else -> {
                            notificationService.deleteNotificationDislike(
                                authenticationService.getUser(),
                                post["userId"].toString(),
                                post.id
                            )

                            notificationService.addNotificationLike(
                                authenticationService.getUser(),
                                post["userId"].toString(),
                                post.id,
                            )
                        }
                    }
                }
            }
        }

        private fun onDislike(updatedPost: DocumentSnapshot, post: DocumentSnapshot) {
            val dislikers = (updatedPost.get("dislikers") as? List<*>)
                ?.mapNotNull { it as? String }
                ?: emptyList()

            authenticationService.getUser()?.let { auth ->
                val isDisliked = dislikers.contains(auth.uid)

                val action =
                    if (isDisliked) postService.unlikeAndDislikePost(post)
                    else postService.dislikePost(post)

                action.addOnSuccessListener {
                    when(isDisliked){
                        true -> {
                            notificationService.deleteNotificationDislike(
                                authenticationService.getUser(),
                                post["userId"].toString(),
                                post.id
                            )
                        }
                        else -> {
                            notificationService.deleteNotificationLike(
                                authenticationService.getUser(),
                                post["userId"].toString(),
                                post.id
                            )

                            notificationService.addNotificationDislike(
                                authenticationService.getUser(),
                                post["userId"].toString(),
                                post.id
                            )
                        }
                    }
                }
            }
        }
    }
}
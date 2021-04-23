package edu.bluejack20_2.braven.domains.post

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

class PostViewHolder(
    private val binding: ItemPostBinding,
    private val fragment: Fragment,
    private val userService: UserService,
    private val postService: PostService,
    private val authenticationService: AuthenticationService,
    private val notificationService: NotificationService

) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: DocumentSnapshot) {
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

        binding.createdAt.text = (post["timestamp"] as? Timestamp)?.toDate().toString()
        binding.title.text = post["title"].toString()
        binding.category.text = post["category"].toString()

        val storageReference = postService.getStorageReference(post["thumbnailId"].toString())
        GlideApp.with(binding.root).load(storageReference).into(binding.thumbnail)

        post.reference.addSnapshotListener { updatedPost, err ->
            err?.let {
                Snackbar.make(
                    fragment.requireActivity().findViewById(R.id.coordinatorLayout),
                    it.toString(),
                    Snackbar.LENGTH_LONG
                ).show()
            }

            binding.like.text = fragment.getString(
                R.string.like,
                updatedPost?.getLong("likersCount") ?: 0
            )

            binding.dislike.text = fragment.getString(
                R.string.dislike,
                updatedPost?.getLong("dislikersCount") ?: 0
            )

            binding.like.setOnClickListener {
                val likers = (updatedPost?.get("likers") as? List<*>)
                    ?.mapNotNull { it as? String }
                    ?: emptyList()

                val isLiked = likers.contains(post["userId"])

                val action =
                    if (isLiked) postService.unlikeAndDislikePost(post)
                    else postService.likePost(post)

                action.addOnSuccessListener {
                    val notification =
                        if (isLiked) "Post un-liked"
                        else "Post liked"

                    Snackbar.make(
                        fragment.requireActivity().findViewById(R.id.coordinatorLayout),
                        notification,
                        Snackbar.LENGTH_LONG
                    ).show()
                    notificationService.deleteNotificationDislike(authenticationService.getUser(), post["userId"].toString(), post["id"].toString())
                    notificationService.addNotificationLike(authenticationService.getUser(), post["userId"].toString(), post["id"].toString(), )
                }
            }

            binding.dislike.setOnClickListener {
                val dislikers = (updatedPost?.get("dislikers") as? List<*>)
                    ?.mapNotNull { it as? String }
                    ?: emptyList()

                val isDisliked = dislikers.contains(post["userId"])

                val action =
                    if (isDisliked) postService.unlikeAndDislikePost(post)
                    else postService.dislikePost(post)

                action.addOnSuccessListener {
                    val notification =
                        if (isDisliked) "Post un-disliked"
                        else "Post disliked"

                    Snackbar.make(
                        fragment.requireActivity().findViewById(R.id.coordinatorLayout),
                        notification,
                        Snackbar.LENGTH_LONG
                    ).show()
                    notificationService.deleteNotificationLike(authenticationService.getUser(), post["userId"].toString(), post["id"].toString())
                    notificationService.addNotificationDislike(authenticationService.getUser(), post["userId"].toString(), post["id"].toString())
                }
            }

            postService.getPostById(post["id"].toString()).addSnapshotListener { it, _ ->
                binding.like.text = fragment.getString(
                    R.string.like,
                    it?.getLong("likersCount") ?: 0
                )
                binding.dislike.text = fragment.getString(
                    R.string.dislike,
                    it?.getLong("dislikersCount") ?: 0
                )
            }
        }
    }
}
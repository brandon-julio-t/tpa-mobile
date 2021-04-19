package edu.bluejack20_2.braven.domains.post

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack20_2.braven.NavGraphDirections
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.databinding.ItemPostBinding
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.modules.GlideApp

class PostViewHolder(
    private val binding: ItemPostBinding,
    private val fragment: Fragment,
    private val userService: UserService,
    private val postService: PostService
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model: DocumentSnapshot) {
        model.data?.let { post ->
            post["id"] = model.id

            binding.cardLayout.setOnClickListener {
                fragment.findNavController().navigate(
                    NavGraphDirections.toPostDetail(post["id"].toString())
                )
            }

            userService.getUserById(post["userId"].toString()).get().addOnSuccessListener {
                it.data?.let { user ->
                    binding.posterDisplayName.text = user["displayName"].toString()

                    user["photoUrl"]?.let { url ->
                        Glide.with(binding.root)
                            .load(url.toString())
                            .into(binding.posterProfilePicture)
                    }
                }
            }

            binding.createdAt.text = (post["timestamp"] as? Timestamp)?.toDate().toString()
            binding.title.text = post["title"].toString()
            binding.category.text = post["category"].toString()

            val path = "thumbnails/${post["thumbnailId"]}"
            val storageReference = FirebaseStorage.getInstance().reference.child(path)
            GlideApp.with(binding.root).load(storageReference).into(binding.thumbnail)

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
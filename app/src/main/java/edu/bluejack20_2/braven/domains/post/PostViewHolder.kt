package edu.bluejack20_2.braven.domains.post

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.databinding.ItemPostBinding
import edu.bluejack20_2.braven.modules.GlideApp
import edu.bluejack20_2.braven.pages.home.HomeFragmentDirections
import edu.bluejack20_2.braven.services.AuthenticationService

class PostViewHolder(
    private val binding: ItemPostBinding,
    private val fragment: Fragment,
    private val authenticationService: AuthenticationService,
    private val postService: PostService
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model: DocumentSnapshot) {
        model.data?.let { post ->
            val timestamp = post["timestamp"] as? Timestamp

            post["id"] = model.id

            binding.cardLayout.setOnClickListener {
                binding.root.findNavController()
                    .navigate(HomeFragmentDirections.toPostDetail(bundleOf("post" to post)))
            }

            authenticationService.getUserById(post["userId"].toString()).addOnSuccessListener {
                it.data?.let { user ->
                    binding.posterDisplayName.text = user["displayName"].toString()

                    user["photoUrl"]?.let { url ->
                        Glide.with(binding.root)
                            .load(url.toString())
                            .into(binding.posterProfilePicture)
                    }
                }
            }

            binding.createdAt.text = timestamp?.toDate().toString()
            binding.title.text = post["title"].toString()
            binding.category.text = post["category"].toString()

            val path = "thumbnails/${model.id}"
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
        }

    }
}
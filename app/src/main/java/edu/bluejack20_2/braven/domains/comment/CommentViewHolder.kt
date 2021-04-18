package edu.bluejack20_2.braven.domains.comment

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.databinding.ItemCommentBinding
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.pages.post_detail.PostDetailFragmentDirections

class CommentViewHolder(
    private val binding: ItemCommentBinding,
    private val userService: UserService
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(fragment: Fragment, comment: DocumentSnapshot) {
        userService.getUserById(comment["userId"].toString()).get().addOnSuccessListener {
            binding.posterDisplayName.text = it["displayName"].toString()

            if (it["photoUrl"] != null) {
                Glide.with(fragment)
                    .load(it["photoUrl"].toString())
                    .into(binding.posterProfilePicture)
            }
        }

        binding.timestamp.text = (comment["timestamp"] as? Timestamp)?.toDate().toString()
        binding.body.text = comment["body"].toString()

        listOf(binding.posterProfilePicture, binding.posterDisplayName, binding.timestamp).forEach {
            it.setOnClickListener {
                fragment.findNavController()
                    .navigate(
                        PostDetailFragmentDirections.postDetailToUserProfile(
                            comment["userId"].toString()
                        )
                    )
            }
        }
    }
}
package edu.bluejack20_2.braven.domains.comment

import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.databinding.ItemCommentBinding
import edu.bluejack20_2.braven.services.AuthenticationService

class CommentViewHolder(
    private val binding: ItemCommentBinding,
    private val authenticationService: AuthenticationService
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(fragment: Fragment, comment: DocumentSnapshot) {
        authenticationService.getUserById(comment["userId"].toString()).addOnSuccessListener {
            binding.posterDisplayName.text = it["displayName"].toString()

            if (it["photoUrl"] != null) {
                Glide.with(fragment)
                    .load(it["photoUrl"].toString())
                    .into(binding.posterProfilePicture)
            }
        }

        binding.body.text = comment["body"].toString()
        binding.timestamp.text = (comment["timestamp"] as? Timestamp)?.toDate().toString()
    }
}
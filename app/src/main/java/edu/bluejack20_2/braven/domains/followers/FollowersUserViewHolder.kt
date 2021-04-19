package edu.bluejack20_2.braven.domains.followers

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.braven.databinding.ItemFollowersBinding

class FollowersUserViewHolder(
    private val binding: ItemFollowersBinding
):RecyclerView.ViewHolder(binding.root) {
    val userName: TextView = binding.usernameText
    val photoProfile: ImageView = binding.profilePictureImage
}
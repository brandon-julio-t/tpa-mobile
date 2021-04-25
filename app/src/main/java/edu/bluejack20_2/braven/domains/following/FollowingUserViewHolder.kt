package edu.bluejack20_2.braven.domains.following

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.braven.databinding.ItemFollowingBinding

class FollowingUserViewHolder(
    binding: ItemFollowingBinding,
):RecyclerView.ViewHolder(binding.root) {

    val userName: TextView = binding.usernameText
    val photoProfile: ImageView = binding.profilePictureImage
    var button: Button = binding.action

}
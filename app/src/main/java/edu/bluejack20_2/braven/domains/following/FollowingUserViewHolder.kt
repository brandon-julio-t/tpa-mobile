package edu.bluejack20_2.braven.domains.following

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.databinding.ItemExploreBinding
import edu.bluejack20_2.braven.databinding.ItemFollowingBinding
import edu.bluejack20_2.braven.domains.user.UserService

class FollowingUserViewHolder(
    private val binding: ItemFollowingBinding,
):RecyclerView.ViewHolder(binding.root) {

    val userName: TextView = binding.usernameText
    val photoProfile: ImageView = binding.profilePictureImage

}
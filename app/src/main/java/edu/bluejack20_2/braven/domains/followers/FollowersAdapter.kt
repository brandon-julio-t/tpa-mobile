package edu.bluejack20_2.braven.domains.followers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.bluejack20_2.braven.databinding.ItemFollowersBinding

class FollowersAdapter(
    private val followersList: List<*>,
    private val userService: FollowersUserService
): RecyclerView.Adapter<FollowersUserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersUserViewHolder {

        val binding = ItemFollowersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowersUserViewHolder(binding)

    }

    override fun onBindViewHolder(holder: FollowersUserViewHolder, position: Int) {
        // dapetin idnya
        val currentItem = followersList[position]

        // dapetin detail followingnya
        userService.getUserByUserId(currentItem as String).get().addOnSuccessListener {
            holder.userName.text = it.get("displayName").toString()

            it["photoUrl"]?.let { url ->
                Glide.with(holder.itemView)
                    .load(url.toString())
                    .into(holder.photoProfile)
            }

        }
    }

    override fun getItemCount(): Int = followersList.size
}
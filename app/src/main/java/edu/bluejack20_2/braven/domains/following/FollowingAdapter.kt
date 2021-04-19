package edu.bluejack20_2.braven.domains.following

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.bluejack20_2.braven.databinding.ItemFollowingBinding

class FollowingAdapter(
    private val followingList: List<*>,
    private val userService: FollowingUserService
    ) : RecyclerView.Adapter<FollowingUserViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FollowingUserViewHolder {
        val binding = ItemFollowingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowingUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowingUserViewHolder, position: Int) {
        // dapetin idnya
        val currentItem = followingList[position]

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

    override fun getItemCount(): Int = followingList.size

}
package edu.bluejack20_2.braven.domains.followers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.bluejack20_2.braven.databinding.ItemFollowersBinding
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AuthenticationService

class FollowersAdapter(
    private val followersList: List<*>,
    private val userServices: FollowersUserService,
    private val userService: UserService,
    private val authenticationService: AuthenticationService
): RecyclerView.Adapter<FollowersUserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersUserViewHolder {

        val binding = ItemFollowersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowersUserViewHolder(binding)

    }

    override fun onBindViewHolder(holder: FollowersUserViewHolder, position: Int) {
        // dapetin idnya
        val currentItem = followersList[position]

        // dapetin CurrentUser
        val currentUser = authenticationService.getUser()

        // dapetin detail followingnya
        userServices.getUserByUserId(currentItem as String).addSnapshotListener { friend, _ ->
            holder.userName.text = friend?.get("displayName").toString()

            friend?.get("photoUrl")?.let { url ->
                Glide.with(holder.itemView)
                    .load(url.toString())
                    .into(holder.photoProfile)
            }

            val friendData = friend?.data
            friendData?.set("id", friend.id)
            val friendFollowing = friendData?.get("followers") as? List<*>

            when(friend?.id == currentUser?.uid){
                true ->
                    holder.button.visibility = View.INVISIBLE

                false -> {
                    friendFollowing?.contains(currentUser?.uid)?.let{
                        when(it){
                            true -> {
                                holder.button.text = "Following"
                                holder.button.setOnClickListener {
                                    userService.unFollow(currentUser, friendData).addOnSuccessListener {
                                        holder.button.text = "Follow"
                                    }
                                }

                            }
                            false -> {
                                holder.button.text = "Follow"
                                holder.button.setOnClickListener {
                                    userService.follow(currentUser, friendData).addOnSuccessListener {
                                        holder.button.text = "following"
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    override fun getItemCount(): Int = followersList.size
}
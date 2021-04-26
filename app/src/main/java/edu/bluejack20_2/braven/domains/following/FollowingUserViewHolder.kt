package edu.bluejack20_2.braven.domains.following

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.databinding.ItemFollowingBinding
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AuthenticationService

class FollowingUserViewHolder(
    private val binding: ItemFollowingBinding,
    private val followingList: List<*>,
    private val userServices: FollowingUserService,
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val loginId: String,
    private val notificationService: NotificationService,
):RecyclerView.ViewHolder(binding.root) {

    fun bind(documentSnapshot: DocumentSnapshot){
        documentSnapshot?.data.let {

            // dapetin detail followingnya
            userServices.getUserByUserId(it!!.get("userId").toString()).addSnapshotListener { friend, _ ->
                binding.usernameText.text = friend?.get("displayName").toString()

                friend?.get("photoUrl")?.let { url ->
                    Glide.with(binding.root)
                        .load(url.toString())
                        .into(binding.profilePictureImage)
                }

                val userIdLogin = authenticationService.getUser()!!.uid

                val friendData = friend?.data
                friendData?.set("id", friend.id)
                val followers = friendData?.get("followers") as? List<*>

                when(friend?.id == userIdLogin){
                    true ->
                        binding.action.visibility = View.INVISIBLE

                    false -> {
                        binding.action.text = "Following"
                        binding.action.setOnClickListener {
                            userService.unFollow(userIdLogin, friendData!!).addOnSuccessListener {
                                binding.action.text = "Follow"
                            }
                            notificationService.deleteNotificationFollow(userIdLogin, friend?.id)
                        }

                        followers?.contains(userIdLogin)?.let {
                            if(!it){
                                binding.action.text = "Follow"
                                binding.action.setOnClickListener {
                                    userService.follow(userIdLogin, friendData).addOnSuccessListener {
                                        binding.action.text = "following"
                                    }

                                    notificationService.addNotificationFollow(authenticationService.getUser(), friend?.id)
                                }

                            }
                        }
                    }
                }

            }
        }
    }

}
package edu.bluejack20_2.braven.domains.notification.notification_following

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.databinding.ItemNotificationFollowingBinding
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AuthenticationService

class NotificationFollowingViewHolder(
    private val binding: ItemNotificationFollowingBinding,
    private val authenticationService: AuthenticationService,
    private val userService: UserService,
    private val notifcationService: NotificationService
): RecyclerView.ViewHolder(binding.root){

        fun bind(documentSnapshot: DocumentSnapshot){


            documentSnapshot.data.let{

                val user = authenticationService.getUser()
                val followersId = it?.get("friendId") as? String
                val time = (it?.get("time") as? Timestamp)?.toDate().toString()

                userService.getUserById(followersId!!).addSnapshotListener{ friend, _ ->

                    if (friend != null) {
                        binding.notificationFollowingUsernameText.text = "${friend.get("displayName").toString()} started following you !"
                    }
                    friend!!.get("photoUrl")?.let { url ->
                        Glide.with(binding.root)
                            .load(url.toString())
                            .into(binding.profilePictureImage)
                    }
                    binding.time.text = time

                    if (user != null) {
                        userService.getUserById(user.uid).get().addOnSuccessListener { userData->
                            val friendFollowing = userData?.get("followings") as? List<*>
                            friendFollowing?.contains(followersId)?.let{ result ->

                                Log.wtf("result", result.toString())

                                when(result){
                                    true ->
                                        unFollowActivity(user, followersId)
                                    false ->
                                        followActivity(user, followersId)
                                }
                            }
                        }
                    }
                 }
            }
        }

    private fun unFollowActivity(user: FirebaseUser, followersId: String){
        binding.actionFollow.text = "Following"
        binding.actionFollow.setOnClickListener {
            userService.unFollow(user, followersId).addOnSuccessListener {
                binding.actionFollow.text = "Follow"
            }
            if (user != null) {
                notifcationService.deleteNotificationFollow(user.uid, followersId)
            }
        }
    }

    private fun followActivity(user: FirebaseUser, followersId: String){
        binding.actionFollow.text = "Follow"
        binding.actionFollow.setOnClickListener {
            userService.follow(user, followersId).addOnSuccessListener {
                binding.actionFollow.text = "Following"
            }
            notifcationService.addNotificationFollow(user, followersId)
        }
    }

}
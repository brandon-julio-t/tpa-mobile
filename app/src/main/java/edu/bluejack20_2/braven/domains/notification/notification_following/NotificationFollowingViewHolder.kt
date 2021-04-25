package edu.bluejack20_2.braven.domains.notification.notification_following

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.databinding.ItemExploreBinding
import edu.bluejack20_2.braven.databinding.ItemNotificationFollowingBinding
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.pages.notification.NotificationFragment
import edu.bluejack20_2.braven.pages.notification.NotificationFragmentDirections
import edu.bluejack20_2.braven.pages.notification.view_pager_fragments.following.NotificationFollowingFragment
import edu.bluejack20_2.braven.pages.post_detail.PostDetailFragmentDirections
import edu.bluejack20_2.braven.services.AuthenticationService

class NotificationFollowingViewHolder(
    private val binding: ItemNotificationFollowingBinding,
    private val authenticationService: AuthenticationService,
    private val userService: UserService,
    private val notifcationService: NotificationService,
    private val fragment: Fragment
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(documentSnapshot: DocumentSnapshot) {


        documentSnapshot?.data.let {

            val user = authenticationService.getUser()
            val followersId = it?.get("friendId") as? String
            val time = (it?.get("time") as? Timestamp)?.toDate().toString()

            userService.getUserById(followersId!!).addSnapshotListener { friend, _ ->

                if (friend != null) {
                    binding.notificationFollowingUsernameText.text =
                        "${friend.get("displayName").toString()} started following you !"
                }
                friend!!.get("photoUrl")?.let { url ->
                    Glide.with(binding.root)
                        .load(url.toString())
                        .into(binding.profilePictureImage)
                }
                binding.time.text = time.toString()

                if (user != null) {
                    userService.getUserById(user.uid).get().addOnSuccessListener { userData ->
                        val friendFollowing = userData?.get("followings") as? List<*>
                        friendFollowing?.contains(followersId)?.let { result ->


                            when (result) {
                                true ->
                                    unFollowActivity(user, followersId)
                                false ->
                                    followActivity(user, followersId)
                            }
                        }
                    }
                }

                listOf(
                    binding.profilePictureImage,
                    binding.notificationFollowingUsernameText
                ).forEach {
                    it.setOnClickListener {
                        fragment.findNavController()
                            .navigate(
                                NotificationFragmentDirections.actionNotificationFragmentToUserProfileFragment(friend.id)
                            )
                    }
                }

            }
        }
    }

    fun unFollowActivity(user: FirebaseUser, followersId: String) {
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

    fun followActivity(user: FirebaseUser, followersId: String) {
        binding.actionFollow.text = "Follow"
        binding.actionFollow.setOnClickListener {
            userService.follow(user, followersId).addOnSuccessListener {
                binding.actionFollow.text = "Following"
            }
            notifcationService.addNotificationFollow(user, followersId)
        }
    }

}
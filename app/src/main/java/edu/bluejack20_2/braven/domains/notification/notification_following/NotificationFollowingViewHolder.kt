package edu.bluejack20_2.braven.domains.notification.notification_following

import android.text.Html
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.databinding.ItemNotificationFollowingBinding
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.pages.notification.NotificationFragmentDirections
import edu.bluejack20_2.braven.services.AuthenticationService
import edu.bluejack20_2.braven.services.TimestampService

class NotificationFollowingViewHolder(
    private val binding: ItemNotificationFollowingBinding,
    private val authenticationService: AuthenticationService,
    private val userService: UserService,
    private val notifcationService: NotificationService,
    private val fragment: Fragment,
    private val timestampService: TimestampService
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(documentSnapshot: DocumentSnapshot) {

        documentSnapshot.data.let {

            val user = authenticationService.getUser()
            val followersId = it?.get("friendId") as? String
            val time = timestampService.prettyTime(it?.get("time") as Timestamp)

            userService.getUserById(followersId!!).addSnapshotListener { friend, _ ->

                    if (friend != null) {
                        val sourceText = fragment.getString(R.string.follow_text, "<b>${friend!!.getString("displayName")}</b>")
                        binding.notificationFollowingUsernameText.text = Html.fromHtml(sourceText)
                    }
                    friend!!.get("photoUrl")?.let { url ->
                        Glide.with(binding.root)
                            .load(url.toString())
                            .into(binding.profilePictureImage)
                    }
                    binding.time.text = time

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

    private fun unFollowActivity(user: FirebaseUser, followersId: String){

        binding.actionFollow.text = fragment.getString(R.string.following)
        binding.actionFollow.setOnClickListener {
            userService.unFollow(user, followersId).addOnSuccessListener {
                binding.actionFollow.text = fragment.getString(R.string.followings)
            }
            if (user != null) {
                notifcationService.deleteNotificationFollow(user.uid, followersId)
            }
        }
    }

    private fun followActivity(user: FirebaseUser, followersId: String){
        val sourceText = fragment.getString(R.string.followings)
        binding.actionFollow.text = sourceText
        binding.actionFollow.setOnClickListener {
            userService.follow(user, followersId).addOnSuccessListener {
                binding.actionFollow.text = fragment.getString(R.string.following)
            }
            notifcationService.addNotificationFollow(user, followersId)
        }
    }

}
package edu.bluejack20_2.braven.domains.followers

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.databinding.ItemFollowersBinding
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.pages.following_page.FollowingUserProfileFragmentDirections
import edu.bluejack20_2.braven.services.AuthenticationService

class FollowersUserViewHolder(
    private val fragment: Fragment,
    private val binding: ItemFollowersBinding,
    private val userServices: FollowersUserService,
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val notificationService: NotificationService,
    private val loginId: String
):RecyclerView.ViewHolder(binding.root) {

    fun bind(documentSnapshot: DocumentSnapshot){
        documentSnapshot?.data.let {

            // dapetin detail followingnya
            userServices.getUserByUserId(it!!.get("userId").toString()).addSnapshotListener { friend, _ ->
                binding.usernameText.text = friend?.get("displayName").toString()

                if(friend!!.get("photoUrl") == null){
                    Glide.with(binding.root)
                        .load(R.drawable.ic_baseline_account_circle_24)
                        .into(binding.profilePictureImage)
                }
                else{
                    friend!!.get("photoUrl")?.let { url ->
                        Glide.with(binding.root)
                            .load(url.toString())
                            .into(binding.profilePictureImage)
                    }
                }

                val userIdLogin = authenticationService.getUser()!!.uid

                val friendData = friend?.data
                friendData?.set("id", friend.id)
                val friendFollowing = friendData?.get("followers") as? List<*>

                when(friend?.id == authenticationService.getUser()!!.uid){
                    true ->
                        binding.action.visibility = View.INVISIBLE

                    false -> {
                        if (friendFollowing == null) {
                            val sourceText = fragment.getString(R.string.followings)
                            binding.action.text = sourceText
                            binding.action.setOnClickListener {
                                userService.follow(userIdLogin, friendData!!).addOnSuccessListener {
                                    binding.action.text = fragment.getString(R.string.following)
                                }

                                notificationService.addNotificationFollow(authenticationService.getUser(), friend.id)
                            }
                        }

                        friendFollowing?.contains(authenticationService.getUser()!!.uid)?.let{
                            when(it){
                                true -> {
                                    binding.action.text = fragment.getString(R.string.following)
                                    binding.action.setOnClickListener {
                                        userService.unFollow(userIdLogin, friendData).addOnSuccessListener {
                                            binding.action.text = fragment.getString(R.string.followings)
                                        }

                                        notificationService.deleteNotificationFollow(authenticationService.getUser()!!.uid, friend?.id)

                                    }
                                }
                                false -> {
                                    val sourceText = fragment.getString(R.string.followings)
                                    binding.action.text = sourceText
                                    binding.action.setOnClickListener {
                                        userService.follow(userIdLogin, friendData).addOnSuccessListener {
                                            binding.action.text = fragment.getString(R.string.following)
                                        }

                                        notificationService.addNotificationFollow(authenticationService.getUser(), friend?.id)
                                    }
                                }
                            }
                        }
                    }
                }

                listOf(
                    binding.cardLayout
                ).forEach {
                    it.setOnClickListener {
                        fragment.findNavController()
                            .navigate(
                                FollowingUserProfileFragmentDirections.toUserProfile(friend!!.id)
                            )
                    }
                }

            }
        }
    }

}
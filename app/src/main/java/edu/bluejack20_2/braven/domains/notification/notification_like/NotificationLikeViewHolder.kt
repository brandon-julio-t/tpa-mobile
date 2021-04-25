package edu.bluejack20_2.braven.domains.notification.notification_like

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.databinding.ItemNotificationLikeBinding
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.pages.notification.NotificationFragmentDirections
import edu.bluejack20_2.braven.services.AuthenticationService
import edu.bluejack20_2.braven.services.TimestampService

class NotificationLikeViewHolder(
    private val binding: ItemNotificationLikeBinding,
    private val authenticationService: AuthenticationService,
    private val userService: UserService,
    private val notifcationService: NotificationService,
    private val postService: PostService,
    private val fragment: Fragment,
    private val timestampService: TimestampService
):RecyclerView.ViewHolder(binding.root) {

    fun bind(documentSnapshot: DocumentSnapshot) {

        documentSnapshot.data.let {

            val user = authenticationService.getUser()
            val postId = it?.get("postId").toString()
            val friendId = it?.get("friendId").toString()
            val time = timestampService.prettyTime(it?.get("time") as Timestamp)

            userService.getUserById(friendId).addSnapshotListener{ data, _ ->

                when(it?.get("like").toString()){
                    "yes" -> {
                        when(user!!.uid == friendId){
                            true -> binding.descText.text = "You Liked Your Post !"
                            else -> binding.descText.text = "${data!!.getString("displayName")} Liked Your Post !"
                        }
                    }
                    "no" -> {
                        when(user!!.uid == friendId){
                            true -> binding.descText.text = "You Disliked Your Post !"
                            else -> binding.descText.text = "${data!!.getString("displayName")} Disliked Your Post !"
                        }
                    }

                }


                data!!.get("photoUrl")?.let { url ->
                    Glide.with(binding.root)
                        .load(url.toString())
                        .into(binding.profilePictureImage)
                }
            }

            binding.timeText.text = time
            postService.getPostById(postId).addSnapshotListener { data, _ ->
                binding.postTitleText.text = "Post Title : ${data!!.getString("title")}"
            }

            binding.cardLayout.setOnClickListener {
                fragment.findNavController().navigate(
                    NotificationFragmentDirections.toPostDetail(postId)
                )
            }

        }

    }


}
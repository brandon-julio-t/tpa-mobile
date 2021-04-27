package edu.bluejack20_2.braven.domains.notification.notification_comment

import android.text.Html
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.databinding.ItemNotificationCommentBinding
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.pages.explore.ExploreFragmentDirections
import edu.bluejack20_2.braven.pages.notification.NotificationFragmentDirections
import edu.bluejack20_2.braven.services.AuthenticationService
import edu.bluejack20_2.braven.services.TimestampService

class NotificationCommentViewHolder(
    private val binding: ItemNotificationCommentBinding,
    private val authenticationService: AuthenticationService,
    private val userService: UserService,
    private val notifcationService: NotificationService,
    private val postService: PostService,
    private val fragment: Fragment,
    private val timestampService: TimestampService

    ): RecyclerView.ViewHolder(binding.root){

        fun bind(documentSnapshot: DocumentSnapshot){
            documentSnapshot?.data?.let{

                val user = authenticationService.getUser()
                val postId = it.get("postId").toString()
                val friendId = it.get("friendId").toString()
                val time = timestampService.prettyTime(it?.get("time") as Timestamp)

                userService.getUserById(friendId).addSnapshotListener { friend, _ ->
                    val sourceText = "<b>${friend!!.getString("displayName")}</b> Commented on Your Post !"
                    binding.usernameText.text = Html.fromHtml(sourceText)

                    friend!!.get("photoUrl")?.let { url ->
                        Glide.with(binding.root)
                            .load(url.toString())
                            .into(binding.profilePictureImage)
                    }
                    binding.time.text = time.toString()
                }

                postService.getPostById(postId).get().addOnSuccessListener {
                    binding.titlePostText.text = "Title Post : ${it.get("title").toString()}"
                }

                binding.cardLayout.setOnClickListener {
                    fragment.findNavController().navigate(
                        NotificationFragmentDirections.toPostDetail(postId)
                    )
                }

            }
        }


}
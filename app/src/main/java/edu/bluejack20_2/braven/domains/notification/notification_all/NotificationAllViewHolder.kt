package edu.bluejack20_2.braven.domains.notification.notification_all

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.databinding.ItemNotificationAllBinding
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.pages.notification.NotificationFragmentDirections
import edu.bluejack20_2.braven.services.AuthenticationService
import edu.bluejack20_2.braven.services.TimestampService

class NotificationAllViewHolder(
    private val binding: ItemNotificationAllBinding,
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val postService: PostService,
    private val notificationService: NotificationService,
    private val fragment: Fragment,
    private val timestampService: TimestampService
): RecyclerView.ViewHolder(binding.root) {

    fun bind(documentSnapshot: DocumentSnapshot){


        documentSnapshot?.data?.let{

            val user = authenticationService.getUser()
            val friendId = it.get("friendId").toString()
            val time = timestampService.prettyTime(it?.get("time") as Timestamp)
            val type = it.get("type").toString()

            userService.getUserById(friendId).addSnapshotListener{ friend, _ ->
                /* name */
                initNameText(type, friend, it, user, friendId)

                /* photo URL */
                initPhotoProfile(friend)

                /* time */
                initTime(time)

                /* post title */
                initPostTitle(type, it)

                /* button */
                initButton(type, user, friendId)

                /* card clicked */
                initClickedDirectPage(type, it, friendId)

            }





        }
    }


    fun initNameText(type: String, friend: DocumentSnapshot?, it: Map<String, Any>, user: FirebaseUser?, friendId: String){
        when(type){
            "follow" -> {
                binding.descText.text = "${friend!!.get("displayName").toString()} started following you !"
            }
            "like" -> {
                when(it?.get("like").toString()){
                    "yes" -> {
                        when(user!!.uid == friendId){
                            true -> binding.descText.text = "You Liked Your Post !"
                            else -> binding.descText.text = "${friend!!.getString("displayName")} Liked Your Post !"
                        }
                    }
                    "no" -> {
                        when(user!!.uid == friendId){
                            true -> binding.descText.text = "You Disliked Your Post !"
                            else -> binding.descText.text = "${friend!!.getString("displayName")} Disliked Your Post !"
                        }
                    }
                }
            }
            "comment" -> {
                binding.descText.text = "${friend!!.getString("displayName")} Commented On Your Post !"
            }
        }
    }

    fun initPhotoProfile(friend: DocumentSnapshot?){
        friend!!.get("photoUrl")?.let { url ->
            Glide.with(binding.root)
                .load(url.toString())
                .into(binding.profilePictureImage)
        }
    }

    fun initTime(time: String){
        binding.timeText.text = time
    }

    fun initPostTitle(type: String, it: Map<String, Any>){
        when(type){
            "follow" ->{
                binding.titlePostText.visibility = View.GONE
            }
            "like" -> {
                binding.titlePostText.visibility = View.VISIBLE
                val postId = it.get("postId").toString()
                postService.getPostById(postId).addSnapshotListener { data, _ ->
                    binding.titlePostText.text = "Post Title : ${data!!.getString("title")}"
                }
            }
            "comment" -> {
                binding.titlePostText.visibility = View.VISIBLE
                val postId = it.get("postId").toString()
                postService.getPostById(postId).addSnapshotListener { data, _ ->
                    binding.titlePostText.text = "Post Title : ${data!!.getString("title")}"
                }
            }
        }
    }

    fun initButton(type: String, user: FirebaseUser?, friendId: String){
        when(type){
            "like" -> {
                binding.actionFollow.visibility = View.GONE
            }
            "comment" -> {
                binding.actionFollow.visibility = View.GONE
            }
            "follow" -> {
                binding.actionFollow.visibility = View.VISIBLE
                if (user != null) {
                    userService.getUserById(user.uid).get().addOnSuccessListener { userData ->
                        val friendFollowing = userData?.get("followings") as? List<*>
                        friendFollowing?.contains(friendId)?.let { result ->


                            when (result) {
                                true ->
                                    unFollowActivity(user, friendId)
                                false ->
                                    followActivity(user, friendId)
                            }
                        }
                    }
                }
            }
        }
    }

    fun initClickedDirectPage(type: String, it: Map<String, Any>, friendId: String){
        when(type){
            "follow" -> {
                listOf(
                    binding.profilePictureImage,
                    binding.descText
                ).forEach {
                    it.setOnClickListener {
                        fragment.findNavController()
                            .navigate(
                                NotificationFragmentDirections.actionNotificationFragmentToUserProfileFragment(friendId)
                            )
                    }
                }
            }
            "like" -> {
                val postId = it.get("postId").toString()
                binding.cardLayout.setOnClickListener {
                    fragment.findNavController().navigate(
                        NotificationFragmentDirections.toPostDetail(postId)
                    )
                }
            }
            "comment" -> {
                val postId = it.get("postId").toString()
                binding.cardLayout.setOnClickListener {
                    fragment.findNavController().navigate(
                        NotificationFragmentDirections.toPostDetail(postId)
                    )
                }
            }
        }
    }

    /* Button Activity */
    fun unFollowActivity(user: FirebaseUser, followersId: String) {
        binding.actionFollow.text = "Following"
        binding.actionFollow.setOnClickListener {
            userService.unFollow(user, followersId).addOnSuccessListener {
                binding.actionFollow.text = "Follow"
            }
            if (user != null) {
                notificationService.deleteNotificationFollow(user.uid, followersId)
            }
        }
    }

    fun followActivity(user: FirebaseUser, followersId: String) {
        binding.actionFollow.text = "Follow"
        binding.actionFollow.setOnClickListener {
            userService.follow(user, followersId).addOnSuccessListener {
                binding.actionFollow.text = "Following"
            }
            notificationService.addNotificationFollow(user, followersId)
        }
    }

}

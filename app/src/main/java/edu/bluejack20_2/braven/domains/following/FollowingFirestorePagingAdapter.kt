package edu.bluejack20_2.braven.domains.following

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.databinding.ItemFollowersBinding
import edu.bluejack20_2.braven.databinding.ItemFollowingBinding
import edu.bluejack20_2.braven.domains.followers.FollowersUserViewHolder
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AuthenticationService

class FollowingFirestorePagingAdapter(
    private val followingList: List<*>,
    private val userServices: FollowingUserService,
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val loginId: String,
    private val notificationService: NotificationService,
    options: FirestorePagingOptions<DocumentSnapshot>
    ) : FirestorePagingAdapter<DocumentSnapshot, FollowingUserViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingUserViewHolder =
        FollowingUserViewHolder(
            ItemFollowingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            followingList,
            userServices,
            userService,
            authenticationService,
            loginId,
            notificationService
        )

    override fun onBindViewHolder(
        holder: FollowingUserViewHolder,
        position: Int,
        model: DocumentSnapshot
    ) = holder.bind(model)


    /*
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
            val followers = friendData?.get("followers") as? List<*>

//
//            Log.wtf("wtf: friend", friend?.id.toString())
//            Log.wtf("wtf: current", currentUser?.uid.toString())
//            Log.wtf("wtf: equals", (friend?.get("id") == currentUser?.uid).toString())

            when(friend?.id == currentUser?.uid){
                true ->
                    holder.button.visibility = View.INVISIBLE

                false -> {
                    holder.button.text = "Following"
                    holder.button.setOnClickListener {
                        userService.unFollow(currentUser, friendData!!).addOnSuccessListener {
                            holder.button.text = "Follow"
                        }
                    }

                    followers?.contains(currentUser?.uid)?.let {
                        if(!it){
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

    override fun getItemCount(): Int = followingList.size

*/
}
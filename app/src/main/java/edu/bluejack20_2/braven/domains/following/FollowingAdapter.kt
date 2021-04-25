package edu.bluejack20_2.braven.domains.following

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.bluejack20_2.braven.databinding.ItemFollowingBinding
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AuthenticationService

class FollowingAdapter(
    private val followingList: List<*>,
    private val userServices: FollowingUserService,
    private val userService: UserService,
    private val authenticationService: AuthenticationService
    ) : RecyclerView.Adapter<FollowingUserViewHolder>() {

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



//    private fun actionButtonUnFollowState(
//        fragment: UserProfileFragment,
//        binding: FragmentUserProfileBinding,
//        currentUser: FirebaseUser?,
//        user: Map<String, Any>
//    ) {
//        binding.action.text = fragment.getString(R.string.unfollow)
//        binding.action.setOnClickListener {
//            userService.unFollow(currentUser, user).addOnSuccessListener {
//                Snackbar.make(
//                    fragment.requireActivity().findViewById(R.id.coordinatorLayout),
//                    "User un-followed",
//                    Snackbar.LENGTH_LONG
//                ).show()
//
//                binding.action.text = fragment.getString(R.string.follow)
//            }
//        }
//    }


}
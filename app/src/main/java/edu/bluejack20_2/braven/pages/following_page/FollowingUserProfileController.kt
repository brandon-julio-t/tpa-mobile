package edu.bluejack20_2.braven.pages.following_page

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack20_2.braven.databinding.FragmentFollowingUserProfileBinding
import edu.bluejack20_2.braven.domains.followers.FollowersFirestorePagingAdapter
import edu.bluejack20_2.braven.domains.following.FollowingFirestorePagingAdapter
import edu.bluejack20_2.braven.domains.following.FollowingUserService
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.factories.FirestorePagingAdapterOptionsFactory
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

class FollowingUserProfileController @Inject constructor(
    private val followingUserService: FollowingUserService,
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val notificationService: NotificationService
) {

    fun bind(fragment: FollowingUserProfileFragment){
        val binding = fragment.binding

        initRecycleView(binding, fragment)
    }

    private fun initRecycleView(binding: FragmentFollowingUserProfileBinding, fragment: FollowingUserProfileFragment){

        val binding = fragment.binding
        val user = authenticationService.getUser()
        val loginId = fragment.requireArguments().getString("auth")

        binding.followingUserRecycleview.layoutManager = LinearLayoutManager(
            fragment.requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )

        userService.getUserById(loginId.toString()).get().addOnSuccessListener {


            var followings = it.get("followings") as List<String>

            followings = followings.toMutableList().also {
                it.add("mrfkejgnejgnrjfnwmekmfkwa")
            }

            val query = followingUserService.getAllUserFollowing(followings)

            binding.followingUserRecycleview.layoutManager = LinearLayoutManager(
                fragment.requireActivity(),
                LinearLayoutManager.VERTICAL,
                false
            )

            binding.followingUserRecycleview.adapter = FollowingFirestorePagingAdapter(
                followings,
                followingUserService,
                userService,
                authenticationService,
                loginId!!,
                notificationService,
                FirestorePagingAdapterOptionsFactory(fragment, query!!).create()
            )
        }
    }


}
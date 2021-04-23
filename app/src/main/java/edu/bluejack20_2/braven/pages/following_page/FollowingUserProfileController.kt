package edu.bluejack20_2.braven.pages.following_page

import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack20_2.braven.databinding.FragmentFollowingUserProfileBinding
import edu.bluejack20_2.braven.domains.following.FollowingAdapter
import edu.bluejack20_2.braven.domains.following.FollowingUserService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

class FollowingUserProfileController @Inject constructor(
    private val followingUserService: FollowingUserService,
    private val userService: UserService,
    private val authenticationService: AuthenticationService
) {

    fun bind(fragment: FollowingUserProfileFragment){
        val binding = fragment.binding

        initRecycleView(binding, fragment)
    }

    private fun initRecycleView(binding: FragmentFollowingUserProfileBinding, fragment: FollowingUserProfileFragment){
        binding.followingUserRecycleview.layoutManager = LinearLayoutManager(
            fragment.requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )

        val loginId = fragment.requireArguments().getString("auth")

        followingUserService.getAllUserFollowing(loginId.toString()).get().addOnSuccessListener {
            val followings = it.get("followings") as? List<*>

            binding.followingUserRecycleview.adapter =
                followings?.let { it1 -> FollowingAdapter(it1, followingUserService, userService, authenticationService) }
        }
    }


}
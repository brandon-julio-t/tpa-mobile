package edu.bluejack20_2.braven.pages.followers_page

import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack20_2.braven.domains.followers.FollowersAdapter
import edu.bluejack20_2.braven.domains.followers.FollowersUserService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

class FollowersUserProfileController @Inject constructor(
    private val followersUserService: FollowersUserService,
    private val userService: UserService,
    private val authenticationService: AuthenticationService
) {

    fun bind(fragment: FollowersUserProfileFragment){
        val binding = fragment.binding

        binding.followersUserRecycleview.layoutManager = LinearLayoutManager(
            fragment.requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )

        val loginId = fragment.requireArguments().getString("auth")

        followersUserService.getAllUserFollowers(loginId as String).get().addOnSuccessListener {
            val followers = it.get("followers") as? List<*>

            binding.followersUserRecycleview.adapter = followers?.let { it1 -> FollowersAdapter(it1, followersUserService, userService, authenticationService) }
        }

    }


}
package edu.bluejack20_2.braven.pages.followers_page

import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack20_2.braven.domains.followers.FollowersAdapter
import edu.bluejack20_2.braven.domains.followers.FollowersUserService
import javax.inject.Inject

class FollowersUserProfileController @Inject constructor(private val followersUserService: FollowersUserService) {

    fun bind(fragment: FollowersUserProfileFragment){
        val binding = fragment.binding

        binding.followingUserRecycleview.layoutManager = LinearLayoutManager(
            fragment.requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )

        val loginId = fragment.requireArguments().getString("auth")

        followersUserService.getAllUserFollowers(loginId as String).get().addOnSuccessListener {
            var followers = it.get("followers") as? List<*>

            binding.followingUserRecycleview.adapter = followers?.let { it1 -> FollowersAdapter(it1, followersUserService) }
        }

    }


}
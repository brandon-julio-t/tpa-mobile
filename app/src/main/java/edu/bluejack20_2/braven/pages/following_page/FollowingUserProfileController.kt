package edu.bluejack20_2.braven.pages.following_page

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.databinding.FragmentFollowingUserProfileBinding
import edu.bluejack20_2.braven.databinding.FragmentUserProfileBinding
import edu.bluejack20_2.braven.domains.following.FollowingAdapter
import edu.bluejack20_2.braven.domains.following.FollowingUserService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.pages.user_profile.UserProfileFragment
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

    fun initRecycleView(binding: FragmentFollowingUserProfileBinding, fragment: FollowingUserProfileFragment){
        binding.followingUserRecycleview.layoutManager = LinearLayoutManager(
            fragment.requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )

        val loginId = fragment.requireArguments().getString("auth")

        followingUserService.getAllUserFollowing(loginId.toString()).get().addOnSuccessListener {
            var followings = it.get("followings") as? List<*>

            binding.followingUserRecycleview.adapter =
                followings?.let { it1 -> FollowingAdapter(it1, followingUserService, userService, authenticationService) }
        }
    }


}
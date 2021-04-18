package edu.bluejack20_2.braven.pages.user_profile

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.databinding.FragmentUserProfileBinding
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.pages.user_profile.view_pager_fragments.UserProfileMostCommentedPostsFragment
import edu.bluejack20_2.braven.pages.user_profile.view_pager_fragments.UserProfileMostLikedPostsFragment
import edu.bluejack20_2.braven.pages.user_profile.view_pager_fragments.UserProfileRecentlyCreatedPostsFragment
import edu.bluejack20_2.braven.pages.user_profile.view_pager_fragments.UserProfileRecentlyLikedPostsFragment
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

class UserProfileController @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val userService: UserService
) {
    fun bind(fragment: UserProfileFragment) {
        val binding = fragment.binding

        userService.getUserById(fragment.args.userId).addSnapshotListener { it, _ ->
            it?.data?.let { user ->
                user["id"] = it.id

                val currentUser = authenticationService.getUser()
                val followers = user["followers"] as? List<*>
                val followings = user["followings"] as? List<*>

                actionButtonFollowState(fragment, binding, currentUser, user)

                followers?.contains(currentUser?.uid)?.let {
                    if (it) {
                        actionButtonUnFollowState(fragment, binding, currentUser, user)
                    }
                }

                if (user["id"] == currentUser?.uid) {
                    actionButtonEditProfileState(fragment, binding)
                }

                binding.postsCount.text = fragment.getString(
                    R.string.posts_count,
                    ((user["posts"] as? List<*>)?.size ?: 0)
                )

                binding.followersCount.text = fragment.getString(
                    R.string.followers_count,
                    followers?.size ?: 0
                )

                binding.followingsCount.text = fragment.getString(
                    R.string.followings_count,
                    followings?.size ?: 0
                )
            }
        }

        userService.getUserById(fragment.args.userId).get().addOnSuccessListener { it ->
            it?.data?.let { user ->
                binding.displayName.text = user["displayName"].toString()

                binding.personalData.text = fragment.getString(
                    R.string.personal_data,
                    user["fullName"].toString(),
                    (user["dateOfBirth"] ?: "Birth date not specified").toString(),
                    user["email"].toString()
                )

                val photoUrl = user["photoUrl"]
                if (!(photoUrl as? String).isNullOrEmpty()) {
                    Glide.with(fragment).load(photoUrl).into(binding.profilePicture)
                }
            }
        }

        val pages = listOf(
            UserProfileMostCommentedPostsFragment(),
            UserProfileMostLikedPostsFragment(),
            UserProfileRecentlyCreatedPostsFragment(),
            UserProfileRecentlyLikedPostsFragment()
        )

        binding.viewPager.adapter = object : FragmentStateAdapter(fragment) {
            override fun getItemCount() = 4
            override fun createFragment(position: Int) = pages[position]
        }
    }

    private fun actionButtonFollowState(
        fragment: UserProfileFragment,
        binding: FragmentUserProfileBinding,
        currentUser: FirebaseUser?,
        user: Map<String, Any>
    ) {
        binding.action.setOnClickListener {
            userService.follow(currentUser, user).addOnSuccessListener {
                Snackbar.make(
                    fragment.requireActivity().findViewById(R.id.coordinatorLayout),
                    "User followed",
                    Snackbar.LENGTH_LONG
                ).show()

                binding.action.text = fragment.getString(R.string.unfollow)
            }
        }
    }

    private fun actionButtonUnFollowState(
        fragment: UserProfileFragment,
        binding: FragmentUserProfileBinding,
        currentUser: FirebaseUser?,
        user: Map<String, Any>
    ) {
        binding.action.text = fragment.getString(R.string.unfollow)
        binding.action.setOnClickListener {
            userService.unFollow(currentUser, user).addOnSuccessListener {
                Snackbar.make(
                    fragment.requireActivity().findViewById(R.id.coordinatorLayout),
                    "User un-followed",
                    Snackbar.LENGTH_LONG
                ).show()

                binding.action.text = fragment.getString(R.string.follow)
            }
        }
    }

    private fun actionButtonEditProfileState(
        fragment: UserProfileFragment,
        binding: FragmentUserProfileBinding
    ) {
        binding.action.text = fragment.getString(R.string.edit_profile)
        binding.action.setOnClickListener {

        }
    }
}
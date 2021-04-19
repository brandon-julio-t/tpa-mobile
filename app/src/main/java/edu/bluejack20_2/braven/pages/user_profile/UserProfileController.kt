package edu.bluejack20_2.braven.pages.user_profile

import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.databinding.FragmentUserProfileBinding
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.pages.following_page.FollowingUserProfileFragmentDirections
import edu.bluejack20_2.braven.pages.post_detail.PostDetailFragmentDirections
import edu.bluejack20_2.braven.pages.user_profile.view_pager_fragments.UserProfileMostCommentedPostsFragment
import edu.bluejack20_2.braven.pages.user_profile.view_pager_fragments.UserProfileMostLikedPostsFragment
import edu.bluejack20_2.braven.pages.user_profile.view_pager_fragments.recent_posts.RecentPostsFragment
import edu.bluejack20_2.braven.pages.user_profile.view_pager_fragments.UserProfileRecentlyLikedPostsFragment
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

class UserProfileController @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val userService: UserService,
    private val postService: PostService
) {
    fun bind(fragment: UserProfileFragment) {
        val binding = fragment.binding
        val query = userService.getUserById(fragment.args.userId)

        directFollowingPage(fragment, binding)
        directFollowersPage(fragment, binding)
        populateData(fragment, binding, query)
        populateRealtimeData(fragment, binding, query)
        setupViewPagerTabs(fragment, binding)
    }


    private fun directFollowingPage(
        fragment: UserProfileFragment,
        binding: FragmentUserProfileBinding
    ) {
        binding.followingsCount.setOnClickListener {
            it.findNavController()
                .navigate(R.id.userProfileToFollowingPage, bundleOf("auth" to fragment.args.userId))
        }
    }

    private fun directFollowersPage(
        fragment: UserProfileFragment,
        binding: FragmentUserProfileBinding
    ) {
        binding.followersCount.setOnClickListener {
            it.findNavController()
                .navigate(R.id.userProfileToFollowersPage, bundleOf("auth" to fragment.args.userId))
        }
    }

    private fun populateData(
        fragment: UserProfileFragment,
        binding: FragmentUserProfileBinding,
        query: DocumentReference
    ) {
        query.get().addOnSuccessListener {
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
    }

    private fun populateRealtimeData(
        fragment: UserProfileFragment,
        binding: FragmentUserProfileBinding,
        query: DocumentReference
    ) {
        query.addSnapshotListener { it, _ ->
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

                postService.getAllPostsByUser(user["id"].toString()).get().addOnSuccessListener {
                    binding.postsCount.text = fragment.getString(R.string.posts_count, it.size())
                }

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
            TODO("Navigate to user profile edit fragment")
        }
    }

    private fun setupViewPagerTabs(
        fragment: UserProfileFragment,
        binding: FragmentUserProfileBinding
    ) {
        val pages = listOf(
            Pair("Recent Posts", RecentPostsFragment(fragment.args.userId)),
            Pair("Recent Likes", UserProfileRecentlyLikedPostsFragment()),
            Pair("Most Comments", UserProfileMostCommentedPostsFragment()),
            Pair("Most Likes", UserProfileMostLikedPostsFragment())
        )

        binding.viewPager.adapter = object : FragmentStateAdapter(fragment) {
            override fun getItemCount() = pages.size
            override fun createFragment(position: Int) = pages[position].second
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = pages[position].first
        }.attach()
    }
}
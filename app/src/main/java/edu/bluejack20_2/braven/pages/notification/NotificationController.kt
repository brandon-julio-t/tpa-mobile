package edu.bluejack20_2.braven.pages.notification

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack20_2.braven.databinding.FragmentNotificationBinding
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.pages.notification.view_pager_fragments.all_notification.NotificationAllFragment
import edu.bluejack20_2.braven.pages.notification.view_pager_fragments.comment.NotificationCommentFragment
import edu.bluejack20_2.braven.pages.notification.view_pager_fragments.following.NotificationFollowingFragment
import edu.bluejack20_2.braven.pages.notification.view_pager_fragments.like.NotificationLikeFragment
import edu.bluejack20_2.braven.pages.user_profile.view_pager_fragments.most_comments.MostCommentsFragment
import edu.bluejack20_2.braven.pages.user_profile.view_pager_fragments.most_likes.MostLikesFragment
import edu.bluejack20_2.braven.pages.user_profile.view_pager_fragments.recent_likes.RecentLikesFragment
import edu.bluejack20_2.braven.pages.user_profile.view_pager_fragments.recent_posts.RecentPostsFragment
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject


class NotificationController @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val userService: UserService
){

    fun bind(fragment: NotificationFragment){
        val binding = fragment.binding

        setupViewPagerTabs(binding, fragment)
    }

    fun setupViewPagerTabs(binding: FragmentNotificationBinding, fragment: NotificationFragment){

        val pages = listOf(
            Pair("Notification", NotificationAllFragment()),
            Pair("Following", NotificationFollowingFragment()),
            Pair("Likes", NotificationLikeFragment()),
            Pair("Comment", NotificationCommentFragment())
        )

        binding.viewPager.adapter = object : FragmentStateAdapter(fragment){
            override fun getItemCount() = pages.size
            override fun createFragment(position: Int) = pages[position].second
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = pages[position].first
        }.attach()

    }

}
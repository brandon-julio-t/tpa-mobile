package edu.bluejack20_2.braven.pages.notification

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import edu.bluejack20_2.braven.databinding.FragmentNotificationBinding
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.pages.notification.view_pager_fragments.all_notification.NotificationAllFragment
import edu.bluejack20_2.braven.pages.notification.view_pager_fragments.comment.NotificationCommentFragment
import edu.bluejack20_2.braven.pages.notification.view_pager_fragments.following.NotificationFollowingFragment
import edu.bluejack20_2.braven.pages.notification.view_pager_fragments.like.NotificationLikeFragment
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject


class NotificationController @Inject constructor() {

    fun bind(fragment: NotificationFragment){
        val binding = fragment.binding

        setupViewPagerTabs(binding, fragment)
    }

    private fun setupViewPagerTabs(binding: FragmentNotificationBinding, fragment: NotificationFragment){

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
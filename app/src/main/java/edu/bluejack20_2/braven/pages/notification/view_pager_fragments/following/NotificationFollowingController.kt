package edu.bluejack20_2.braven.pages.notification.view_pager_fragments.following

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.notification.notification_following.NotificationFollowingFirestorePagingAdapter
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.factories.FirestorePagingAdapterOptionsFactory
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

class NotificationFollowingController @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val userService: UserService,
    private val notificationService: NotificationService
){

    fun bind(fragment: NotificationFollowingFragment){
        val binding = fragment.binding
        val user = authenticationService.getUser()

        val query = notificationService.getNotificationFollow(user?.uid.toString())

        binding.notificationFollowingRecycleview.layoutManager = object : LinearLayoutManager(fragment.requireActivity()) {
            override fun canScrollVertically(): Boolean = false
        }

        if (user != null) {
            binding.notificationFollowingRecycleview.adapter = NotificationFollowingFirestorePagingAdapter(
                fragment,
                userService,
                authenticationService,
                notificationService,
                FirestorePagingAdapterOptionsFactory(fragment, query!!).create()
            )
        }
    }


}
package edu.bluejack20_2.braven.pages.notification.view_pager_fragments.all_notification

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.notification.notification_all.NotificationAllFirestorePagingAdapter
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.factories.FirestorePagingAdapterOptionsFactory
import edu.bluejack20_2.braven.services.AuthenticationService
import edu.bluejack20_2.braven.services.TimestampService
import javax.inject.Inject

class NotificationAllController @Inject constructor(
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val postService: PostService,
    private val notificationService: NotificationService,
    private val timestampService: TimestampService
) {

    fun bind(fragment: NotificationAllFragment){

        val binding = fragment.binding
        val user = authenticationService.getUser()
        val query = notificationService.getNotificationAll(user?.uid.toString())

        binding.notificationAllRecycleview.layoutManager = LinearLayoutManager(fragment.requireActivity())



        binding.notificationAllRecycleview.adapter = NotificationAllFirestorePagingAdapter(
            fragment,
            userService,
            authenticationService,
            postService,
            notificationService,
            timestampService,
            FirestorePagingAdapterOptionsFactory(fragment, query!!).create()
        )

    }


}
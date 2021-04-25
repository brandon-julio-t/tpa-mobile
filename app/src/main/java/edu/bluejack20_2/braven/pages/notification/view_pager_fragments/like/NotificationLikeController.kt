package edu.bluejack20_2.braven.pages.notification.view_pager_fragments.like

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.notification.notification_following.NotificationFollowingFirestorePagingAdapter
import edu.bluejack20_2.braven.domains.notification.notification_like.NotificationLikeFirestorePagingAdapter
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.factories.FirestorePagingAdapterOptionsFactory
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

class NotificationLikeController @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val userService: UserService,
    private val notificationService: NotificationService,
    private val postService: PostService
){

    fun bind(fragment: NotificationLikeFragment){

        val binding = fragment.binding
        val user = authenticationService.getUser()

        val query = notificationService.getNotificationLike(user?.uid.toString())

        binding.notificationLikeRecycleview.layoutManager = LinearLayoutManager(fragment.requireActivity())

        binding.notificationLikeRecycleview.adapter = NotificationLikeFirestorePagingAdapter(
            fragment,
            userService,
            authenticationService,
            notificationService,
            postService,
            FirestorePagingAdapterOptionsFactory(fragment, query!!).create()
        )

    }

}
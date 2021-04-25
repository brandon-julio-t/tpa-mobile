package edu.bluejack20_2.braven.pages.notification.view_pager_fragments.comment

import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.notification.notification_comment.NotificationCommentFirestorePagingAdapter
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.factories.FirestorePagingAdapterOptionsFactory
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

class NotificationCommentController @Inject constructor(
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val postService: PostService,
    private val notificationService: NotificationService
){

    fun bind(fragment: NotificationCommentFragment){

        val binding = fragment.binding
        val user = authenticationService.getUser()
        val query = notificationService.getNotificationComment(user?.uid.toString())

        binding.notificationCommentRecycleview.layoutManager = LinearLayoutManager(fragment.requireActivity())

        binding.notificationCommentRecycleview.adapter = NotificationCommentFirestorePagingAdapter(
            fragment,
            userService,
            authenticationService,
            notificationService,
            postService,
            FirestorePagingAdapterOptionsFactory(fragment, query!!).create()
        )

    }

}
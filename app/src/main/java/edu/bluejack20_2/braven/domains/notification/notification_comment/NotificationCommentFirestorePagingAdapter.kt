package edu.bluejack20_2.braven.domains.notification.notification_comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.databinding.ItemNotificationCommentBinding
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AuthenticationService

class NotificationCommentFirestorePagingAdapter(
    private val fragment: Fragment,
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val notificationService: NotificationService,
    private val postService: PostService,
    options: FirestorePagingOptions<DocumentSnapshot>
): FirestorePagingAdapter<DocumentSnapshot, NotificationCommentViewHolder>(options) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationCommentViewHolder =
        NotificationCommentViewHolder(
            ItemNotificationCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            authenticationService,
            userService,
            notificationService,
            postService,
            fragment
        )

    override fun onBindViewHolder(
        holder: NotificationCommentViewHolder,
        position: Int,
        model: DocumentSnapshot
    ) = holder.bind(model)


}
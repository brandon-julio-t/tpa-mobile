package edu.bluejack20_2.braven.domains.notification.notification_like

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.databinding.ItemNotificationLikeBinding
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AuthenticationService
import edu.bluejack20_2.braven.services.TimestampService

class NotificationLikeFirestorePagingAdapter(
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val notificationService: NotificationService,
    private val postService: PostService,
    private val fragment: Fragment,
    private val timestampService: TimestampService,
    options: FirestorePagingOptions<DocumentSnapshot>
) : FirestorePagingAdapter<DocumentSnapshot, NotificationLikeViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationLikeViewHolder {

        return NotificationLikeViewHolder(
            ItemNotificationLikeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            authenticationService,
            userService,
            notificationService,
            postService,
            fragment,
            timestampService
        )
    }

    override fun onBindViewHolder(
        holder: NotificationLikeViewHolder,
        position: Int,
        model: DocumentSnapshot
    ) = holder.bind(model)
}
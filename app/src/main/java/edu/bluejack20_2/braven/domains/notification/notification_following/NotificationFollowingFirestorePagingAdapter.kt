package edu.bluejack20_2.braven.domains.notification.notification_following

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.databinding.ItemNotificationFollowingBinding
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AuthenticationService
import edu.bluejack20_2.braven.services.TimestampService

class NotificationFollowingFirestorePagingAdapter(
    private val fragment: Fragment,
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val notificationService: NotificationService,
    private val timestampService: TimestampService,
    options: FirestorePagingOptions<DocumentSnapshot>
): FirestorePagingAdapter<DocumentSnapshot, NotificationFollowingViewHolder>(options)  {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationFollowingViewHolder {
        return NotificationFollowingViewHolder(
            ItemNotificationFollowingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            authenticationService,
            userService,
            notificationService,
            fragment,
            timestampService
        )
    }


    override fun onBindViewHolder(
        holder: NotificationFollowingViewHolder,
        position: Int,
        model: DocumentSnapshot
    ) = holder.bind(model)
}
package edu.bluejack20_2.braven.domains.notification.notification_following

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.databinding.ItemNotificationFollowingBinding
import edu.bluejack20_2.braven.domains.explore.ExploreViewHolder
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AuthenticationService

class NotificationFollowingFirestorePagingAdapter(
    private val fragment: Fragment,
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val notificationService: NotificationService,
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
            fragment
        )
    }


    override fun onBindViewHolder(
        holder: NotificationFollowingViewHolder,
        position: Int,
        model: DocumentSnapshot
    ) = holder.bind(model)
}
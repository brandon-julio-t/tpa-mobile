package edu.bluejack20_2.braven.domains.notification.notification_all

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.databinding.ItemNotificationAllBinding
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.notification.notification_comment.NotificationCommentViewHolder
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AuthenticationService
import edu.bluejack20_2.braven.services.TimestampService

class NotificationAllFirestorePagingAdapter(
    private val fragment: Fragment,
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val postService: PostService,
    private val notificationService: NotificationService,
    private val timestampService: TimestampService,
    options: FirestorePagingOptions<DocumentSnapshot>
): FirestorePagingAdapter<DocumentSnapshot, NotificationAllViewHolder>(options)  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAllViewHolder {
        Log.wtf("On Create view holder", "view")

        return NotificationAllViewHolder(
            ItemNotificationAllBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            userService,
            authenticationService,
            postService,
            notificationService,
            fragment,
            timestampService
        )
    }

    override fun onBindViewHolder(
        holder: NotificationAllViewHolder,
        position: Int,
        model: DocumentSnapshot
    ) = holder.bind(model)
}
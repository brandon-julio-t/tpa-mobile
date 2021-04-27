package edu.bluejack20_2.braven.domains.followers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.databinding.ItemFollowersBinding
import edu.bluejack20_2.braven.domains.explore.ExploreViewHolder
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AuthenticationService

class FollowersFirestorePagingAdapter(
    private val fragment: Fragment,
    private val userServices: FollowersUserService,
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val loginId: String,
    private val notificationService: NotificationService,
    options: FirestorePagingOptions<DocumentSnapshot>
): FirestorePagingAdapter<DocumentSnapshot, FollowersUserViewHolder>(options){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersUserViewHolder =
        FollowersUserViewHolder(
            fragment,
            ItemFollowersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            userServices,
            userService,
            authenticationService,
            notificationService,
            loginId
        )


    override fun onBindViewHolder(
        holder: FollowersUserViewHolder,
        position: Int,
        model: DocumentSnapshot
    ) = holder.bind(model)

}
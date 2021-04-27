package edu.bluejack20_2.braven.domains.following

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.databinding.ItemFollowersBinding
import edu.bluejack20_2.braven.databinding.ItemFollowingBinding
import edu.bluejack20_2.braven.domains.followers.FollowersUserService
import edu.bluejack20_2.braven.domains.followers.FollowersUserViewHolder
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

class FollowingUserViewHolderModule @Inject constructor(
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val followingUserService: FollowingUserService,
    private val notificationService: NotificationService,
) {

    inner class ViewHolder(
        private val binding: ItemFollowingBinding,
        private val fragment: Fragment,
        private val loginId: String
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(document: DocumentSnapshot){
            FollowingUserViewHolder(
                fragment,
                binding,
                followingUserService,
                userService,
                authenticationService,
                notificationService,
            ).bind(document)
        }
    }


}
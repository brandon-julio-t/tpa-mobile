package edu.bluejack20_2.braven.domains.followers

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.databinding.ItemFollowersBinding
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

class FollowersUserViewHolderModule @Inject constructor(
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val followersUserService: FollowersUserService,
    private val notificationService: NotificationService,

) {

    inner class ViewHolder(
        private val binding: ItemFollowersBinding,
        private val fragment: Fragment,
        private val loginId: String
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(document: DocumentSnapshot){
            FollowersUserViewHolder(
                fragment,
                binding,
                followersUserService,
                userService,
                authenticationService,
                notificationService,
                loginId
            ).bind(document)
        }
    }


}
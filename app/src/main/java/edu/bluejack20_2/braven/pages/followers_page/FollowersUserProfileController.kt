package edu.bluejack20_2.braven.pages.followers_page

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import edu.bluejack20_2.braven.domains.followers.FollowersFirestorePagingAdapter
import edu.bluejack20_2.braven.domains.followers.FollowersUserService
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.factories.FirestorePagingAdapterOptionsFactory
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

class FollowersUserProfileController @Inject constructor(
    private val followersUserService: FollowersUserService,
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val notificationService: NotificationService
) {

    fun bind(fragment: FollowersUserProfileFragment){
        val binding = fragment.binding
        val user = authenticationService.getUser()

        val loginId = fragment.requireArguments().getString("auth")

        userService.getUserById(loginId.toString()).get().addOnSuccessListener {
            val followers = it.get("followers") as List<String>

            val query = followersUserService.getAllUserFollowers(followers)

            followersUserService.getAllUserFollowers(followers).get().addOnSuccessListener {
                Log.wtf("query Size : ", followers.toString())
            }


            binding.followersUserRecycleview.layoutManager = LinearLayoutManager(
                fragment.requireActivity(),
                LinearLayoutManager.VERTICAL,
                false
            )

            binding.followersUserRecycleview.adapter = FollowersFirestorePagingAdapter(
                followersUserService,
                userService,
                authenticationService,
                loginId!!,
                notificationService,
                FirestorePagingAdapterOptionsFactory(fragment, query!!).create()
            )
        }

    }


}
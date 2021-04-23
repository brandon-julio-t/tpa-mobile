package edu.bluejack20_2.braven.pages.home

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.post.PostFirestorePagingAdapter
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.factories.FirestorePagingAdapterOptionsFactory
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

class HomeController @Inject constructor(
    private val postService: PostService,
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val notificationService: NotificationService
) {
    fun bind(fragment: HomeFragment) {
        val binding = fragment.binding

        binding.posts.layoutManager = LinearLayoutManager(
            fragment.requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )

        binding.posts.adapter = PostFirestorePagingAdapter(
            fragment,
            userService,
            postService,
            authenticationService,
            notificationService,
            FirestorePagingAdapterOptionsFactory(
                fragment,
                postService.getAllPosts()
            ).create()
        )

        binding.createPost.setOnClickListener {
            fragment.findNavController().navigate(HomeFragmentDirections.homeToPostCreate())
        }
    }
}
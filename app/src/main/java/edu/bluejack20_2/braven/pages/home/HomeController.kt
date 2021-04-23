package edu.bluejack20_2.braven.pages.home

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.post.PostFirestorePagingAdapter
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.factories.FirestorePagingAdapterOptionsFactory
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject


class HomeController @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val postService: PostService,
    private val userService: UserService,
    private val notificationService: NotificationService
) {
    private lateinit var fragment: HomeFragment
    private val binding get() = fragment.binding

    fun bind(fragment: HomeFragment) {
        this.fragment = fragment

        binding.createPost.setOnClickListener {
            fragment.findNavController().navigate(HomeFragmentDirections.homeToPostCreate())
        }

        setupPostsSuperRecyclerView()
    }

    private fun setupPostsSuperRecyclerView() {
        binding.posts.layoutManager = LinearLayoutManager(
            fragment.requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )

        authenticationService.getUser()?.let { auth ->
            userService.getUserById(auth.uid).get().addOnSuccessListener { user ->
                val query = postService.getAllFollowingsPosts(user)

                binding.posts.adapter = PostFirestorePagingAdapter(
                    fragment,
                    userService,
                    postService,
                    authenticationService,
                    notificationService,
                    FirestorePagingAdapterOptionsFactory(
                        fragment.viewLifecycleOwner,
                        query
                    ).create()
                )
            }
        }
    }
}
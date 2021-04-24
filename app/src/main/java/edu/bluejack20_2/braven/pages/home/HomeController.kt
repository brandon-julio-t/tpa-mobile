package edu.bluejack20_2.braven.pages.home

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack20_2.braven.domains.post.PostFirestorePagingAdapterModule
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.factories.FirestorePagingAdapterOptionsFactory
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

class HomeController @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val postService: PostService,
    private val userService: UserService,
    private val postFirestorePagingAdapterModule: PostFirestorePagingAdapterModule
) {
    fun bind(fragment: HomeFragment) {
        val binding = fragment.binding

        binding.createPost.setOnClickListener {
            fragment.findNavController().navigate(HomeFragmentDirections.homeToPostCreate())
        }

        setupPostsSuperRecyclerView(fragment)
    }

    private fun setupPostsSuperRecyclerView(fragment: HomeFragment) {
        val binding = fragment.binding

        binding.posts.layoutManager = LinearLayoutManager(fragment.requireActivity())

        authenticationService.getUser()?.let { auth ->
            userService.getUserById(auth.uid).get().addOnSuccessListener { user ->
                binding.posts.adapter = postFirestorePagingAdapterModule.Adapter(
                    fragment,
                    postService.getAllFollowingsPosts(user)
                )
            }
        }
    }
}
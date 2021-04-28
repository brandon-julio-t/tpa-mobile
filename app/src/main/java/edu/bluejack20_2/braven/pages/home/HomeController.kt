package edu.bluejack20_2.braven.pages.home

import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack20_2.braven.domains.post.PostFirestorePagingAdapterModule
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

class HomeController @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val postService: PostService,
    private val userService: UserService,
    private val postFirestorePagingAdapterModule: PostFirestorePagingAdapterModule
) {
    private lateinit var adapter: PostFirestorePagingAdapterModule.Adapter

    fun bind(fragment: HomeFragment) {
        val binding = fragment.binding

        binding.posts.setRefreshListener { adapter.refresh() }

        binding.posts.setLayoutManager(LinearLayoutManager(fragment.requireActivity()))

        authenticationService.getUser()?.let { auth ->
            userService.getUserById(auth.uid).get().addOnSuccessListener { user ->
                adapter = postFirestorePagingAdapterModule.Adapter(
                    fragment,
                    postService.getAllFollowingsPosts(user)
                )

                binding.posts.adapter = adapter
            }
        }
    }
}
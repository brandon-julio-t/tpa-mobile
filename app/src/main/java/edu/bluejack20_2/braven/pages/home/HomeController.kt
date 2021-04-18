package edu.bluejack20_2.braven.pages.home

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack20_2.braven.domains.post.PostFirestorePagingAdapter
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.factories.FirestorePagingAdapterOptionFactory
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

class HomeController @Inject constructor(
    private val postService: PostService,
    private val authenticationService: AuthenticationService
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
            authenticationService,
            postService,
            FirestorePagingAdapterOptionFactory(
                fragment,
                postService.getAllPosts()
            ).create()
        )

        binding.createPost.setOnClickListener {
            fragment.findNavController().navigate(HomeFragmentDirections.toPostCreate())
        }
    }
}
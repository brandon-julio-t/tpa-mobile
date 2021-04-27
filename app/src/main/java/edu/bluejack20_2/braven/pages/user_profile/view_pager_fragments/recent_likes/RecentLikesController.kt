package edu.bluejack20_2.braven.pages.user_profile.view_pager_fragments.recent_likes

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentReference
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.domains.post.PostViewHolderModule
import edu.bluejack20_2.braven.domains.user.UserService
import javax.inject.Inject

class RecentLikesController @Inject constructor(
    private val userService: UserService,
    private val postViewHolderModule: PostViewHolderModule
) {
    fun bind(fragment: RecentLikesFragment) {
        val binding = fragment.binding

        if (fragment.userId == null) {
            fragment.findNavController().navigate(R.id.homeFragment)
            return
        }

        userService.getUserById(fragment.userId).get().addOnSuccessListener { snapshot ->
            val likedPosts = snapshot.get("likedPosts")?.let { posts ->
                val list = posts as List<*>
                list.mapNotNull { it as? DocumentReference }.asReversed()
            } ?: emptyList()

            binding.posts.adapter = RecentLikesAdapter(fragment, likedPosts, postViewHolderModule)
        }

        binding.posts.layoutManager = object : LinearLayoutManager(fragment.requireActivity()) {
            override fun canScrollVertically(): Boolean = false
        }
    }
}
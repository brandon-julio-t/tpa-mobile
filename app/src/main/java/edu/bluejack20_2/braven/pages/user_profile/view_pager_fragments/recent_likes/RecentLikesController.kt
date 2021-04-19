package edu.bluejack20_2.braven.pages.user_profile.view_pager_fragments.recent_likes

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentReference
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.domains.user.UserService
import javax.inject.Inject

class RecentLikesController @Inject constructor(
    private val userService: UserService,
    private val postService: PostService
) {
    fun bind(fragment: RecentLikesFragment) {
        val binding = fragment.binding

        userService.getUserById(fragment.userId!!).get().addOnSuccessListener { snapshot ->
            val likedPosts = snapshot.get("likedPosts")?.let { posts ->
                val list = posts as List<*>
                list.mapNotNull { it as? DocumentReference }.asReversed()
            } ?: emptyList()

            binding.posts.adapter = RecentLikesAdapter(
                fragment,
                userService,
                postService,
                likedPosts
            )
        }

        binding.posts.layoutManager = object : LinearLayoutManager(fragment.requireActivity()) {
            override fun canScrollVertically(): Boolean = false
        }
    }
}
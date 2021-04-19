package edu.bluejack20_2.braven.pages.user_profile.view_pager_fragments.most_likes

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import edu.bluejack20_2.braven.domains.post.PostFirestorePagingAdapter
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.factories.FirestorePagingAdapterOptionsFactory
import javax.inject.Inject

class MostLikesController @Inject constructor(
    private val userService: UserService,
    private val postService: PostService
) {
    fun bind(fragment: MostLikesFragment) {
        val binding = fragment.binding
        val query = postService.getAllPostsByUser(fragment.userId)
            .orderBy("likersCount", Query.Direction.DESCENDING)
            .orderBy("timestamp", Query.Direction.DESCENDING)

        binding.posts.layoutManager = object : LinearLayoutManager(fragment.requireActivity()) {
            override fun canScrollVertically(): Boolean = false
        }

        binding.posts.adapter = PostFirestorePagingAdapter(
            fragment,
            userService,
            postService,
            FirestorePagingAdapterOptionsFactory(fragment, query).create()
        )
    }
}
package edu.bluejack20_2.braven.pages.user_profile.view_pager_fragments.recent_posts

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import edu.bluejack20_2.braven.domains.post.PostFirestorePagingModule
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.factories.FirestorePagingAdapterOptionsFactory
import javax.inject.Inject

class RecentPostsController @Inject constructor(
    private val postService: PostService,
    private val postFirestorePagingModule: PostFirestorePagingModule
) {
    fun bind(fragment: RecentPostsFragment) {
        val binding = fragment.binding

        val query = postService.getAllPostsByUser(fragment.userId!!)
            .orderBy("timestamp", Query.Direction.DESCENDING)

        binding.posts.layoutManager = object : LinearLayoutManager(fragment.requireActivity()) {
            override fun canScrollVertically(): Boolean = false
        }

        binding.posts.adapter = postFirestorePagingModule.Adapter(
            fragment,
            FirestorePagingAdapterOptionsFactory(fragment, query).create()
        )
    }
}
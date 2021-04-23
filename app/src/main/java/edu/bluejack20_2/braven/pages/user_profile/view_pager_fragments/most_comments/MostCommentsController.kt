package edu.bluejack20_2.braven.pages.user_profile.view_pager_fragments.most_comments

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import edu.bluejack20_2.braven.domains.post.PostFirestorePagingModule
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.factories.FirestorePagingAdapterOptionsFactory
import javax.inject.Inject

class MostCommentsController @Inject constructor(
    private val postService: PostService,
    private val postFirestorePagingModule: PostFirestorePagingModule
) {
    fun bind(fragment: MostCommentsFragment) {
        val binding = fragment.binding
        val query = postService.getAllPostsByUser(fragment.userId)
            .orderBy("commentsCount", Query.Direction.DESCENDING)
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
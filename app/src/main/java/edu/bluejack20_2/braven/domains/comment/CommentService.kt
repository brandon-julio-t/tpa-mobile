package edu.bluejack20_2.braven.domains.comment

import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import edu.bluejack20_2.braven.domains.post.PostService
import javax.inject.Inject

class CommentService @Inject constructor(
    private val repository: CommentRepository,
    private val postService: PostService
) {
    fun getAllCommentsByPost(post: Map<*, *>): Query {
        return repository.allByPost(post["id"].toString())
            .orderBy("timestamp", Query.Direction.DESCENDING)
    }

    fun createComment(
        post: Map<*, *>,
        user: FirebaseUser?,
        comment: String
    ): Task<DocumentReference> {
        val data = hashMapOf(
            "body" to comment,
            "postId" to post["id"].toString(),
            "userId" to user?.uid.toString(),
            "timestamp" to Timestamp.now()
        )

        postService.incrementCommentsCount(post["id"].toString())
        return repository.save(data)
    }
}
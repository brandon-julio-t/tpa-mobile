package edu.bluejack20_2.braven.domains.comment

import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import javax.inject.Inject

class CommentService @Inject constructor(private val repository: CommentRepository) {
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

        return repository.save(data)
    }

    fun getAllCommentsByPost(post: Map<*, *>): Query {
        return repository.allByPost(post["id"].toString())
            .orderBy("timestamp", Query.Direction.DESCENDING)
    }
}
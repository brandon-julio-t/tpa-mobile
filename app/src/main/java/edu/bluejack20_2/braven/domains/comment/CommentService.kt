package edu.bluejack20_2.braven.domains.comment

import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

class CommentService @Inject constructor(
    private val repository: CommentRepository,
    private val authenticationService: AuthenticationService,
    private val postService: PostService
) {
    fun getAllCommentsByPost(post: Map<*, *>): Query =
        repository.allByPost(post["id"].toString()).orderBy("timestamp", Query.Direction.DESCENDING)

    fun createComment(
        post: Map<*, *>,
        user: FirebaseUser?,
        comment: String
    ): Task<DocumentReference> {
        val data = hashMapOf(
            "body" to comment,
            "postId" to post["id"].toString(),
            "post" to postService.getDocumentReference(post["id"].toString()),
            "userId" to user?.uid.toString(),
            "user" to authenticationService.getCurrentUserDocumentReference(),
            "timestamp" to Timestamp.now()
        )

        postService.incrementCommentsCount(post["id"].toString())
        return repository.save(data)
    }
}
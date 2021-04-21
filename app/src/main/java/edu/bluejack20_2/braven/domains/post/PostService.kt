package edu.bluejack20_2.braven.domains.post

import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AuthenticationService
import java.util.*
import javax.inject.Inject

class PostService @Inject constructor(
    private val repository: PostRepository,
    private val authenticationService: AuthenticationService
) {
    fun getPostById(id: String) = repository.getById(id)

    fun getAllPostsByUser(userId: String) = repository.getByUser(userId)

    fun getAllPostsByUserBetweenTimestamp(userId: String, start: Timestamp, end: Timestamp) =
        getAllPostsByUser(userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .whereGreaterThanOrEqualTo("timestamp", start)
            .whereLessThanOrEqualTo("timestamp", end)

    fun getStorageReference(id: String) = repository.getStorageReferenceById(id)

    fun createPost(
        title: String,
        description: String,
        category: String,
        thumbnail: ByteArray
    ): Task<Unit> {
        val data = hashMapOf(
            "title" to title,
            "description" to description,
            "category" to category,
            "userId" to authenticationService.getUser()?.uid.toString(),
            "thumbnailId" to UUID.randomUUID().toString(),
            "timestamp" to FieldValue.serverTimestamp()
        )

        return repository.save(data, thumbnail)
    }

    fun updatePost(
        postId: String,
        title: String,
        description: String,
        category: String,
        thumbnail: ByteArray,
        oldThumbnailId: String
    ): Task<Unit> {
        val data = hashMapOf(
            "title" to title,
            "description" to description,
            "category" to category,
            "userId" to authenticationService.getUser()?.uid.toString(),
            "thumbnailId" to UUID.randomUUID().toString(),
        )

        return repository.update(postId, data, thumbnail, oldThumbnailId)
    }

    fun likePost(post: DocumentSnapshot) =
        repository.like(post.id, authenticationService.getUser()?.uid.toString())

    fun unlikeAndDislikePost(post: DocumentSnapshot) =
        repository.unlikeAndDislike(post.id, authenticationService.getUser()?.uid.toString())

    fun dislikePost(post: DocumentSnapshot) =
        repository.dislike(post.id, authenticationService.getUser()?.uid.toString())

    fun incrementCommentsCount(id: String) =
        repository.update(id, hashMapOf("commentsCount" to FieldValue.increment(1)))
}
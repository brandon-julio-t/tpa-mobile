package edu.bluejack20_2.braven.domains.post

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import edu.bluejack20_2.braven.services.AuthenticationService
import java.util.*
import javax.inject.Inject

class PostService @Inject constructor(
    private val repository: PostRepository,
    private val authenticationService: AuthenticationService,
) {
    fun getDocumentReference(id: String) = repository.getDocumentReference(id)

    fun getAllPosts() = repository.getAll()

    fun getPostById(id: String) = repository.getById(id)

    fun getAllPostsByUser(userId: String) = repository.getByUser(userId)

    fun getStorageReference(id: String): StorageReference = repository.getStorageReferenceById(id)

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
            "userId" to authenticationService.getCurrentUser()?.uid.toString(),
            "user" to authenticationService.getCurrentUserDocumentReference(),
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
            "userId" to authenticationService.getCurrentUser()?.uid.toString(),
            "user" to authenticationService.getCurrentUserDocumentReference(),
            "thumbnailId" to UUID.randomUUID().toString(),
        )

        return repository.update(postId, data, thumbnail, oldThumbnailId)
    }

    fun likePost(post: Map<*, *>) =
        repository.like(
            post["id"].toString(),
            authenticationService.getCurrentUser()?.uid.toString()
        )

    fun dislikePost(post: Map<*, *>) =
        repository.dislike(
            post["id"].toString(),
            authenticationService.getCurrentUser()?.uid.toString()
        )

    fun incrementCommentsCount(id: String) {
        repository.update(id, hashMapOf("commentsCount" to FieldValue.increment(1)))
    }
}
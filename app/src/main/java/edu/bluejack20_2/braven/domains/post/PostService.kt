package edu.bluejack20_2.braven.domains.post

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.storage.StorageReference
import edu.bluejack20_2.braven.services.AuthenticationService
import java.util.*
import javax.inject.Inject

class PostService @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val repository: PostRepository,
) {
    fun getAllPosts() = repository.getAll()

    fun getPostById(id: String) = repository.getById(id)

    fun getStorageReference(id: String): StorageReference = repository.getStorageReferenceById(id)

    fun createPost(
        title: String,
        description: String,
        category: String,
        thumbnail: ByteArray
    ): Task<Task<DocumentReference>> {
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
            "timestamp" to FieldValue.serverTimestamp()
        )

        return repository.update(postId, data, thumbnail, oldThumbnailId)
    }

    fun likePost(post: Map<*, *>) =
        repository.like(
            post["id"].toString(),
            authenticationService.getUser()?.uid.toString()
        )

    fun dislikePost(post: Map<*, *>) =
        repository.dislike(
            post["id"].toString(),
            authenticationService.getUser()?.uid.toString()
        )
}
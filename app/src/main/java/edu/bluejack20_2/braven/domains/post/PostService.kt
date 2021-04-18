package edu.bluejack20_2.braven.domains.post

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageReference
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

class PostService @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val repository: PostRepository,
) {
    fun getAllPosts(): Query = repository.all()

    fun getStorageReference(id: String): StorageReference = repository.storageReference(id)

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
            "timestamp" to FieldValue.serverTimestamp()
        )

        return repository.save(data, thumbnail)
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
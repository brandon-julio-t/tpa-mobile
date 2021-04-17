package edu.bluejack20_2.braven.services

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.storage.UploadTask
import edu.bluejack20_2.braven.repositories.PostRepository
import java.util.*
import javax.inject.Inject

class PostService @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val postRepository: PostRepository,
) {
    fun createPost(
        title: String,
        description: String,
        category: String,
        thumbnail: ByteArray
    ): Pair<Task<DocumentReference>, UploadTask> {
        val thumbnailId = UUID.randomUUID().toString()

        val data = hashMapOf(
            "title" to title,
            "description" to description,
            "category" to category,
            "thumbnailId" to thumbnailId,
            "userId" to authenticationService.getUser()?.uid.toString(),
            "timestamp" to FieldValue.serverTimestamp()
        )

        return postRepository.create(data, Pair(thumbnailId, thumbnail))
    }
}
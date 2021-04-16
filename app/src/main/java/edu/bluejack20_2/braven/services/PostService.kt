package edu.bluejack20_2.braven.services

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import edu.bluejack20_2.braven.repositories.PostRepository
import javax.inject.Inject

class PostService @Inject constructor(private val postRepository: PostRepository) {
    fun createPost(title: String, description: String, category: String): Task<DocumentReference> {
        val data = hashMapOf(
            "title" to title,
            "description" to description,
            "category" to category,
            "timestamp" to FieldValue.serverTimestamp()
        )

        return postRepository.create(data)
    }
}
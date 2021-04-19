package edu.bluejack20_2.braven.domains.comment

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import javax.inject.Inject

class CommentRepository @Inject constructor() {
    private val path = "comments"
    private val firestore = FirebaseFirestore.getInstance()
    private val db = firestore.collection(path)

    fun allByPost(postId: String): Query = db.whereEqualTo("postId", postId)

    fun save(data: Map<*, *>): Task<DocumentReference> = db.add(data)
}
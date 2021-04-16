package edu.bluejack20_2.braven.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class PostRepository @Inject constructor() {
    private val db get() = FirebaseFirestore.getInstance().collection("posts")

    fun create(data: HashMap<String, Any>): Task<DocumentReference> {
        return db.add(data)
    }
}
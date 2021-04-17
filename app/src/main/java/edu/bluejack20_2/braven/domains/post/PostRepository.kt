package edu.bluejack20_2.braven.domains.post

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

class PostRepository @Inject constructor() {
    private val db get() = FirebaseFirestore.getInstance().collection("posts")
    private val storage get() = FirebaseStorage.getInstance().reference
    private val storageRoot = "thumbnails"

    fun all(): Query = db.orderBy("timestamp", Query.Direction.DESCENDING)

    fun storageReference(id: String): StorageReference = storage.child("${storageRoot}/${id}")

    fun save(
        data: HashMap<String, Any>,
        thumbnail: ByteArray
    ): Task<Task<DocumentReference>> {
        return db.add(data).continueWith {
            it.addOnSuccessListener { doc ->
                if (thumbnail.isNotEmpty()) {
                    storageReference(doc.id).putBytes(thumbnail)
                }
            }
        }
    }
}
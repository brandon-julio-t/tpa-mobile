package edu.bluejack20_2.braven.domains.post

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class PostRepository @Inject constructor() {
    private val db get() = FirebaseFirestore.getInstance().collection("posts")
    private val storage get() = FirebaseStorage.getInstance().reference
    private val storageRoot = "thumbnails"

    fun getAll() = db.orderBy("timestamp", Query.Direction.DESCENDING)

    fun getStorageReferenceById(id: String) = storage.child("${storageRoot}/${id}")

    fun getById(id: String) = db.document(id)

    fun save(
        data: HashMap<*, *>,
        thumbnail: ByteArray
    ) =
        db.add(data).continueWith {
            if (thumbnail.isNotEmpty()) {
                getStorageReferenceById(data["thumbnailId"].toString()).putBytes(thumbnail)
            }
        }

    fun update(id: String, data: HashMap<String, *>, thumbnail: ByteArray, oldThumbnailId: String) =
        db.document(id).update(data).continueWith {
            if (thumbnail.isNotEmpty()) {
                getStorageReferenceById(oldThumbnailId).delete()
                getStorageReferenceById(data["thumbnailId"].toString()).putBytes(thumbnail)
            }
        }

    fun like(id: String, userId: String) =
        db.document(id).update("likers", FieldValue.arrayUnion(userId)).continueWithTask {
            db.document(id).update("dislikers", FieldValue.arrayRemove(userId))
        }

    fun dislike(id: String, userId: String) =
        db.document(id).update("dislikers", FieldValue.arrayUnion(userId)).continueWithTask {
            db.document(id).update("likers", FieldValue.arrayRemove(userId))
        }
}
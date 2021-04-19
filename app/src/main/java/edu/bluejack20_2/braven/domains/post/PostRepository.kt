package edu.bluejack20_2.braven.domains.post

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class PostRepository @Inject constructor() {
    private val firestore get() = FirebaseFirestore.getInstance()
    private val db get() = firestore.collection("posts")
    private val storage get() = FirebaseStorage.getInstance().reference
    private val storageRoot = "thumbnails"

    fun getAll() = db.orderBy("timestamp", Query.Direction.DESCENDING)

    fun getStorageReferenceById(id: String) = storage.child("${storageRoot}/${id}")

    fun getById(id: String) = db.document(id)

    fun getByUser(userId: String) = db.whereEqualTo("userId", userId)

    fun save(
        data: HashMap<*, *>,
        thumbnail: ByteArray
    ) = db.add(data).continueWith {
        if (thumbnail.isNotEmpty()) {
            getStorageReferenceById(data["thumbnailId"].toString()).putBytes(thumbnail)
        }
    }

    fun update(id: String, data: Map<String,*>) = db.document(id).update(data)

    fun update(id: String, data: Map<String, *>, thumbnail: ByteArray, oldThumbnailId: String) =
        db.document(id).update(data).continueWith {
            if (thumbnail.isNotEmpty()) {
                getStorageReferenceById(oldThumbnailId).delete()
                getStorageReferenceById(data["thumbnailId"].toString()).putBytes(thumbnail)
            }
        }

    fun like(id: String, userId: String): Task<Void> {
        return firestore.runBatch {
            val document = db.document(id)

            document.update("likers", FieldValue.arrayUnion(userId)).continueWith {
                it.addOnSuccessListener {
                    document.get().addOnSuccessListener { post ->
                        updateLikersDislikersCount(post, document)
                    }
                }
            }

            document.update("dislikers", FieldValue.arrayRemove(userId)).continueWith {
                it.addOnSuccessListener {
                    document.get().addOnSuccessListener { post ->
                        updateLikersDislikersCount(post, document)
                    }
                }
            }
        }
    }

    private fun updateLikersDislikersCount(
        post: DocumentSnapshot,
        document: DocumentReference
    ) {
        val likers = post.get("likers") as? List<*>
        val dislikers = post.get("dislikers") as? List<*>

        document.update("likersCount", likers?.size)
        document.update("dislikersCount", dislikers?.size)
    }

    fun dislike(id: String, userId: String): Task<Void> {
        return firestore.runBatch {
            val document = db.document(id)

            document.update("dislikers", FieldValue.arrayUnion(userId)).continueWith {
                it.addOnSuccessListener {
                    document.get().addOnSuccessListener { post ->
                        updateLikersDislikersCount(post, document)
                    }
                }
            }

            document.update("likers", FieldValue.arrayRemove(userId)).continueWith {
                it.addOnSuccessListener {
                    document.get().addOnSuccessListener { post ->
                        updateLikersDislikersCount(post, document)
                    }
                }
            }
        }
    }
}
package edu.bluejack20_2.braven.domains.post

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack20_2.braven.domains.user.UserService
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val userService: UserService
    ) {
    private val firestore = FirebaseFirestore.getInstance()
    private val db = firestore.collection("posts")
    private val storage = FirebaseStorage.getInstance().reference
    private val storageRoot = "thumbnails"

    fun getStorageReferenceById(id: String) = storage.child("${storageRoot}/${id}")

    fun getById(id: String) = db.document(id)

    fun getByUser(userId: String) = db.whereEqualTo("userId", userId)

    fun getAllFollowingsPosts(followings: List<String>): Query {
        if(followings.isEmpty()) {
            return db.whereIn("userId", listOf("grmekagmarklmflawmeioqjiofmkvfkanreuhfiaovmkl"))
                .orderBy("timestamp", Query.Direction.DESCENDING)
        }
        return db.whereIn("userId", followings)
            .orderBy("timestamp", Query.Direction.DESCENDING)
    }

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
                    document.get().addOnSuccessListener { post -> updateLikersDislikersCount(post) }
                }
            }

            document.update("dislikers", FieldValue.arrayRemove(userId)).continueWith {
                it.addOnSuccessListener {
                    document.get().addOnSuccessListener { post -> updateLikersDislikersCount(post) }
                }
            }

            userService.addLikedPost(userId, document)
        }
    }


    private fun updateLikersDislikersCount(post: DocumentSnapshot) {
        val likers = post.get("likers") as? List<*>
        val dislikers = post.get("dislikers") as? List<*>

        post.reference.update("likersCount", likers?.size ?: 0)
        post.reference.update("dislikersCount", dislikers?.size ?: 0)
    }

    fun unlikeAndDislike(postId: String, userId: String) = firestore.runBatch {
        val document = db.document(postId)
        it.update(document, "likers", FieldValue.arrayRemove(userId))
        it.update(document, "dislikers", FieldValue.arrayRemove(userId))
        userService.removeLikedPost(userId, document)
    }.addOnSuccessListener {
        db.document(postId).get().addOnSuccessListener { post -> updateLikersDislikersCount(post) }
    }

    fun dislike(id: String, userId: String): Task<Void> {
        return firestore.runBatch {
            val document = db.document(id)

            document.update("dislikers", FieldValue.arrayUnion(userId)).continueWith {
                it.addOnSuccessListener {
                    document.get().addOnSuccessListener { post -> updateLikersDislikersCount(post) }
                }
            }

            document.update("likers", FieldValue.arrayRemove(userId)).continueWith {
                it.addOnSuccessListener {
                    document.get().addOnSuccessListener { post -> updateLikersDislikersCount(post) }
                }
            }

            userService.removeLikedPost(userId, document)
        }
    }
}
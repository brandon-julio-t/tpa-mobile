package edu.bluejack20_2.braven.domains.notification

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class NotificationService @Inject constructor(){

    private val firestore = FirebaseFirestore.getInstance()
    private val db = FirebaseFirestore.getInstance().collection("notification")
    private val storage = FirebaseStorage.getInstance().reference
    private val storageRoot = "thumbnails"

    /* NOTIFICATION FOLLOW */

    fun getNotificationFollow(userId: String): Query =
        db.whereEqualTo("userId", userId).whereEqualTo("type", "follow").orderBy("time", Query.Direction.DESCENDING)


    fun addNotificationFollow(me: FirebaseUser?, you: Map<*, *>): Task<Void> {
        val data = hashMapOf(
            "userId"  to you["id"].toString(),
            "friendId" to me?.uid.toString(),
            "time" to FieldValue.serverTimestamp(),
            "type" to "follow"
        )
        return firestore.runBatch{
            db.add(data).addOnSuccessListener {}
        }
    }

    fun addNotificationFollow(me: FirebaseUser?, you: String): Task<Void> {
        val data = hashMapOf(
            "userId"  to you,
            "friendId" to me?.uid.toString(),
            "time" to FieldValue.serverTimestamp(),
            "type" to "follow"
        )
        return firestore.runBatch{
            db.add(data).addOnSuccessListener {}
        }
    }

    fun deleteNotificationFollow(myId: String, yourId: String): Task<Void> = firestore.runBatch{
        var documentReference = db.whereEqualTo("type", "follow").whereEqualTo("userId", yourId).whereEqualTo("friendId", myId).get()
            .addOnSuccessListener {
            for (document in it)
                document.reference.delete()
        }
    }

    /* NOTIFICATION LIKE */

    fun getNotificationLike(userId: String): Query =
        db.whereEqualTo("userId", userId).whereEqualTo("type", "like").orderBy("time", Query.Direction.DESCENDING)

    fun addNotificationLike(me: FirebaseUser?, you: String, postId: String): Task<Void> {
        val data = hashMapOf(
            "userId"  to you,
            "friendId" to me?.uid.toString(),
            "postId" to postId,
            "time" to FieldValue.serverTimestamp(),
            "type" to "like",
            "like" to "yes"
        )

        return firestore.runBatch{
            db.add(data).addOnSuccessListener {}
        }
    }

    fun deleteNotificationLike(me: FirebaseUser?, you: String, postId: String): Unit{
        db.whereEqualTo("userId", you).whereEqualTo("friendId", me?.uid.toString()).whereEqualTo("postId", postId)
            .whereEqualTo("type", "like").whereEqualTo("like", "yes").get()
            .addOnSuccessListener {
                for (document in it)
                    document.reference.delete()
            }
    }

    fun addNotificationDislike(me: FirebaseUser?, you: String, postId: String): Task<Void> {
        val data = hashMapOf(
            "userId"  to you,
            "friendId" to me?.uid.toString(),
            "postId" to postId,
            "time" to FieldValue.serverTimestamp(),
            "type" to "like",
            "like" to "no"
        )

        return firestore.runBatch{
            db.add(data).addOnSuccessListener {}
        }
    }

    fun deleteNotificationDislike(me: FirebaseUser?, you: String, postId: String): Unit{
        db.whereEqualTo("userId", you).whereEqualTo("friendId", me?.uid.toString()).whereEqualTo("postId", postId)
            .whereEqualTo("type", "like").whereEqualTo("like", "no").get()
            .addOnSuccessListener {
                for (document in it)
                    document.reference.delete()
            }
    }

}
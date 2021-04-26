package edu.bluejack20_2.braven.domains.notification

import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class NotificationService @Inject constructor(){

    private val firestore = FirebaseFirestore.getInstance()
    private val db = FirebaseFirestore.getInstance().collection("notification")

    /* STATISTIC FOLLOW */
    fun getAllNotificationFollowByUserBetweenTimestamp(userId: String, start: Timestamp, end: Timestamp) =
        getNotificationFollow(userId)
            .whereGreaterThanOrEqualTo("time", start)
            .whereLessThanOrEqualTo("time", end)

    /* STATISTIC LIKE */
    fun getAllNotificationLikeByUserBetweenTimestamp(userId: String, start: Timestamp, end: Timestamp) =
        getNotificationLike(userId)
            .whereGreaterThanOrEqualTo("time", start)
            .whereLessThanOrEqualTo("time", end)

    /* STATISTIC COMMENT */
    fun getAllNotificationCommentByUserBetweenTimestamp(userId: String, start: Timestamp, end: Timestamp) =
        getNotificationComment(userId)
            .whereGreaterThanOrEqualTo("time", start)
            .whereLessThanOrEqualTo("time", end)

    /* NOTIFICATION ALL */

    fun getNotificationAll(userId: String): Query =
        db.whereEqualTo("userId", userId).orderBy("time", Query.Direction.DESCENDING)

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
                for (document in it.documents)
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
                for (document in it.documents)
                    document.reference.delete()
            }
    }

    /* NOTIFICATION COMMENT */

    fun getNotificationComment(userId: String): Query =
        db.whereEqualTo("userId", userId).whereEqualTo("type", "comment").orderBy("time", Query.Direction.DESCENDING)

    fun addNotificationComment(me: FirebaseUser?, post: DocumentSnapshot, postId: String, commentId: String): Task<Void> {
        val data = hashMapOf(
            "userId"  to post["userId"].toString(),
            "friendId" to me?.uid.toString(),
            "postId" to postId,
            "commentId" to commentId,
            "time" to FieldValue.serverTimestamp(),
            "type" to "comment"
        )

        return firestore.runBatch{
            db.add(data).addOnSuccessListener {}
        }
    }

    /*
    fun deleteNotificationComment(me: FirebaseUser?, you: String, postId: String, commentId: String): Unit{
        db.whereEqualTo("userId", you).whereEqualTo("postId", postId)
            .whereEqualTo("commentId", commentId).whereEqualTo("type", "comment").get()
            .addOnSuccessListener {
                for (document in it)
                    document.reference.delete()
            }
    }
    */


}
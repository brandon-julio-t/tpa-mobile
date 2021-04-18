package edu.bluejack20_2.braven.domains.user

import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.*
import javax.inject.Inject

class UserRepository @Inject constructor() {
    private val firestore get() = FirebaseFirestore.getInstance()
    private val db get() = firestore.collection("users")

    fun getById(id: String) = db.document(id)

    fun follow(myId: String, yourId: String): Task<Void> = firestore.runBatch {
        db.document(myId).update("followings", FieldValue.arrayUnion(yourId))
        db.document(yourId).update("followers", FieldValue.arrayUnion(myId))
    }

    fun unFollow(myId: String, yourId: String): Task<Void> = firestore.runBatch {
        db.document(myId).update("followings", FieldValue.arrayRemove(yourId))
        db.document(yourId).update("followers", FieldValue.arrayRemove(myId))
    }

    fun save(user: FirebaseUser): Task<Void> {
        val data = hashMapOf(
            "displayName" to user.displayName?.toString(),
            "fullName" to user.displayName?.toString(),
            "phoneNumber" to user.phoneNumber?.toString(),
            "email" to user.email?.toString(),
            "photoUrl" to user.photoUrl?.toString(),
            "isEmailVerified" to user.isEmailVerified,
            "createdAt" to Timestamp(Date(user.metadata?.creationTimestamp ?: 0)),
            "lastLoginAt" to Timestamp(Date(user.metadata?.lastSignInTimestamp ?: 0)),
        )

        return db.document(user.uid).set(data, SetOptions.merge())
    }
}
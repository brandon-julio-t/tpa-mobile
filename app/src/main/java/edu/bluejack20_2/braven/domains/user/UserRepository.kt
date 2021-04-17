package edu.bluejack20_2.braven.domains.user

import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import javax.inject.Inject

class UserRepository @Inject constructor() {
    private val db get() = FirebaseFirestore.getInstance().collection("users")

    fun getById(id: String): Task<DocumentSnapshot> = db.document(id).get()

    fun save(user: FirebaseUser): Task<Void> {
        val data = hashMapOf(
            "displayName" to user.displayName?.toString(),
            "phoneNumber" to user.phoneNumber?.toString(),
            "email" to user.email?.toString(),
            "photoUrl" to user.photoUrl?.toString(),
            "isEmailVerified" to user.isEmailVerified,
            "createdAt" to Timestamp(Date(user.metadata?.creationTimestamp ?: 0)),
            "lastLoginAt" to Timestamp(Date(user.metadata?.lastSignInTimestamp ?: 0)),
        )

        return db.document(user.uid).set(data)
    }
}
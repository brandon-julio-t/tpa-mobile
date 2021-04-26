package edu.bluejack20_2.braven.domains.user

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import javax.inject.Inject

class UserRepository @Inject constructor() {
    private val firestore = FirebaseFirestore.getInstance()
    private val db = firestore.collection("users")
    private val storage = FirebaseStorage.getInstance().reference
    private val storageRoot = "profilePictures"

    private fun getStorageReferenceById(id: String) = storage.child("${storageRoot}/${id}")

    fun getById(id: String) = db.document(id)

    fun follow(myId: String, yourId: String): Task<Void> = firestore.runBatch {
        db.document(myId).update("followings", FieldValue.arrayUnion(yourId))
        db.document(yourId).update("followers", FieldValue.arrayUnion(myId))
    }

    fun unFollow(myId: String, yourId: String): Task<Void> = firestore.runBatch {
        db.document(myId).update("followings", FieldValue.arrayRemove(yourId))
        db.document(yourId).update("followers", FieldValue.arrayRemove(myId))
    }

    fun update(userId: String, data: Map<String, *>) = db.document(userId).update(data)

    fun updateProfile(
        username: String,
        biography: String,
        password: String
    ): Task<MutableList<Void>>? {
        return FirebaseAuth.getInstance().currentUser?.let { user ->
            val tasks = mutableListOf(db.document(user.uid).update("biography", biography))

            if (password.isNotBlank()) {
                tasks.add(user.updatePassword(password))
            }

            if (username.isNotBlank()) {
                tasks.add(
                    user.updateProfile(
                        UserProfileChangeRequest.Builder().setDisplayName(username).build()
                    )
                )
            }

            Tasks.whenAllSuccess(tasks)
        }
    }

    fun updateProfilePicture(profilePicture: ByteArray, onUpdateSuccess: () -> Unit) {
        if (profilePicture.isNotEmpty()) {
            getStorageReferenceById(UUID.randomUUID().toString()).putBytes(profilePicture)
                .addOnSuccessListener {
                    it.storage.downloadUrl.addOnSuccessListener {
                        FirebaseAuth.getInstance().currentUser?.updateProfile(
                            UserProfileChangeRequest.Builder().setPhotoUri(it).build()
                        )?.addOnSuccessListener {
                            onUpdateSuccess()
                        }
                    }
                }
        }
    }

    fun save(user: FirebaseUser): Task<Void> {
        val lastLogin = user.metadata?.lastSignInTimestamp
        val createdAt = user.metadata?.creationTimestamp

        val data = hashMapOf(
            "displayName" to user.displayName?.toString(),
            "fullName" to user.displayName?.toString(),
            "email" to user.email?.toString(),
            "photoUrl" to user.photoUrl?.toString(),
            "isEmailVerified" to user.isEmailVerified,
            "userId" to user.uid,
            "createdAt" to if (createdAt != null) Timestamp(Date(createdAt)) else FieldValue.serverTimestamp(),
            "lastLoginAt" to if (lastLogin != null) Timestamp(Date(lastLogin)) else FieldValue.serverTimestamp(),
        )

        return db.document(user.uid).set(data, SetOptions.merge())
    }
}
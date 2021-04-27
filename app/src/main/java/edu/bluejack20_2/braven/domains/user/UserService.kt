package edu.bluejack20_2.braven.domains.user

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import javax.inject.Inject

class UserService @Inject constructor(private val repository: UserRepository) {
    fun getUserById(id: String) = repository.getById(id)

    fun follow(me: FirebaseUser?, you: Map<*, *>): Task<Void> =
        repository.follow(me?.uid.toString(), you["id"].toString())

    fun follow(me: FirebaseUser?, you: String): Task<Void> =
        repository.follow(me?.uid.toString(), you)

    fun follow(me: String, you: Map<*, *>): Task<Void> =
        repository.follow(me, you["id"].toString())

    fun unFollow(me: FirebaseUser?, you: Map<*, *>): Task<Void> =
        repository.unFollow(me?.uid.toString(), you["id"].toString())

    fun unFollow(me: FirebaseUser?, you: String): Task<Void> =
        repository.unFollow(me?.uid.toString(), you)

    fun unFollow(me: String, you: Map<*, *>): Task<Void> =
        repository.unFollow(me, you["id"].toString())

    fun updateProfile(username: String, biography: String, password: String) =
        repository.updateProfile(username, biography, password)?.addOnSuccessListener {
            FirebaseAuth.getInstance().currentUser?.let { save(it) }
        }

    fun updateProfilePicture(profilePicture: ByteArray) =
        repository.updateProfilePicture(profilePicture) {
            FirebaseAuth.getInstance().currentUser?.let {
                save(it)
            }
        }

    fun addLikedPost(userId: String, post: DocumentReference): Task<Void> {
        val data = hashMapOf(
            "likedPosts" to FieldValue.arrayUnion(post)
        )

        return repository.update(userId, data)
    }

    fun removeLikedPost(userId: String, post: DocumentReference): Task<Void> {
        val data = hashMapOf(
            "likedPosts" to FieldValue.arrayRemove(post)
        )

        return repository.update(userId, data)
    }

    fun save(user: FirebaseUser) = repository.save(user)
}
package edu.bluejack20_2.braven.domains.user

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import javax.inject.Inject

class UserService @Inject constructor(private val repository: UserRepository) {
    fun getDocumentReference(id: String) = repository.getDocumentReference(id)

    fun getUserById(id: String) = repository.getById(id)

    fun follow(me: FirebaseUser?, you: Map<*, *>): Task<Void> =
        repository.follow(me?.uid.toString(), you["id"].toString())

    fun unFollow(me: FirebaseUser?, you: Map<*, *>): Task<Void> =
        repository.unFollow(me?.uid.toString(), you["id"].toString())

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
package edu.bluejack20_2.braven.domains.user

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class UserService @Inject constructor(private val repository: UserRepository) {
    fun getUserById(id: String) = repository.getById(id)

    fun follow(me: FirebaseUser?, you: Map<*, *>): Task<Void> =
        repository.follow(me?.uid.toString(), you["id"].toString())

    fun unFollow(me: FirebaseUser?, you: Map<*, *>): Task<Void> =
        repository.unFollow(me?.uid.toString(), you["id"].toString())

    fun save(user: FirebaseUser) = repository.save(user)
}
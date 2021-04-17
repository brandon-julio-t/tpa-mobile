package edu.bluejack20_2.braven.services

import android.view.View
import androidx.navigation.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.domains.user.UserRepository
import javax.inject.Inject

class AuthenticationService @Inject constructor(private val userRepository: UserRepository) {
    fun getUser(): FirebaseUser? = FirebaseAuth.getInstance().currentUser

    fun getUserById(userId: String): Task<DocumentSnapshot> = userRepository.getById(userId)

    fun persist() {
        getUser()?.let {
            userRepository.save(it)
        }
    }

    fun logout(view: View) {
        AuthUI.getInstance().signOut(view.context).addOnSuccessListener {
            view.findNavController().navigate(R.id.toLogin)
        }
    }
}
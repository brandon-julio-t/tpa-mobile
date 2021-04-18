package edu.bluejack20_2.braven.services

import android.view.View
import androidx.navigation.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import edu.bluejack20_2.braven.NavGraphDirections
import edu.bluejack20_2.braven.domains.user.UserService
import javax.inject.Inject

class AuthenticationService @Inject constructor(private val userService: UserService) {
    fun getUser(): FirebaseUser? = FirebaseAuth.getInstance().currentUser

    fun persist() {
        getUser()?.let { userService.save(it) }
    }

    fun logout(view: View) {
        AuthUI.getInstance().signOut(view.context).addOnSuccessListener {
            view.findNavController().navigate(NavGraphDirections.toLogin())
        }
    }
}
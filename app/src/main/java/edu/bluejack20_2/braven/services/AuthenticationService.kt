package edu.bluejack20_2.braven.services

import android.view.View
import androidx.navigation.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import edu.bluejack20_2.braven.R
import javax.inject.Inject

class AuthenticationService @Inject constructor() {
    fun getUser(): FirebaseUser? = FirebaseAuth.getInstance().currentUser

    fun logout(view: View) {
        AuthUI.getInstance().signOut(view.context).addOnSuccessListener {
            view.findNavController().navigate(R.id.toLogin)
        }
    }
}
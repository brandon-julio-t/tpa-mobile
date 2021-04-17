package edu.bluejack20_2.braven.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import edu.bluejack20_2.braven.R

class LoginFragment : Fragment() {
    private lateinit var loginActivityLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                val response = IdpResponse.fromResultIntent(it.data)

                if (it.resultCode == Activity.RESULT_OK) {
                    findNavController().navigate(R.id.toHome)
                } else {
                    // Sign in failed. If response is null the user canceled the
                    // sign-in flow using the back button. Otherwise check
                    // response.getError().getErrorCode() and handle the error.
                    // ...

                    Snackbar.make(
                        requireActivity().findViewById(R.id.coordinatorLayout),
                        response?.error.toString(),
                        Snackbar.LENGTH_LONG
                    )
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
        )

        try {
            AuthUI.getInstance().silentSignIn(requireContext(), providers)
                .addOnSuccessListener {
                    findNavController().navigate(R.id.toHome)
                }
                .addOnFailureListener {
                    loginActivityLauncher.launch(
                        AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build()
                    )
                }
        } catch (e: IllegalArgumentException) {
            // user already signed in
            Log.wtf("hehe", e.toString())
            findNavController().navigate(R.id.toHome)
        }
    }
}
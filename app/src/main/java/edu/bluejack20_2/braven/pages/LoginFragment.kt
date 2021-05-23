package edu.bluejack20_2.braven.pages

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
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack20_2.braven.NavGraphDirections
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var loginActivityLauncher: ActivityResultLauncher<Intent>

    @Inject
    lateinit var authenticationService: AuthenticationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                val response = IdpResponse.fromResultIntent(it.data)

                if (it.resultCode == Activity.RESULT_OK) {
                    authenticationService.persist()
                    findNavController().navigate(NavGraphDirections.toHome())
                } else {
                    // Sign in failed. If response is null the user canceled the
                    // sign-in flow using the back button. Otherwise check
                    // response.getError().getErrorCode() and handle the error.
                    // ...

                    Snackbar.make(
                        requireActivity().findViewById(R.id.coordinatorLayout),
                        response?.error.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
                    authenticationService.persist()
                    findNavController().navigate(NavGraphDirections.toHome())
                }
                .addOnFailureListener {
                    loginActivityLauncher.launch(
                        AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setAvailableProviders(providers)
                            .build()
                    )
                }
        } catch (e: IllegalArgumentException) {
            // user already signed in
            authenticationService.persist()
            findNavController().navigate(NavGraphDirections.toHome())
        }
    }
}
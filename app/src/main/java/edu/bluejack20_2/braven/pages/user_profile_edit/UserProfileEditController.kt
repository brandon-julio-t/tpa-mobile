package edu.bluejack20_2.braven.pages.user_profile_edit

import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AuthenticationService
import edu.bluejack20_2.braven.services.ImageMediaService
import javax.inject.Inject

class UserProfileEditController @Inject constructor(
    private val imageMediaService: ImageMediaService,
    private val authenticationService: AuthenticationService,
    private val userService: UserService
) {

    private lateinit var fragment: UserProfileEditFragment
    private var auth: FirebaseUser? = null


    fun bind(fragment: UserProfileEditFragment) {
        this.fragment = fragment
        val viewModel = fragment.viewModel

        auth = authenticationService.getUser()
        fragment.binding.usernameEditText.setText(auth?.displayName.toString())

        fragment.viewModel.profilePicture.observe(fragment.viewLifecycleOwner) {

            if (it.isEmpty()) {
                fragment.binding.previewProfileImage.visibility = View.GONE
                return@observe
            }

            fragment.binding.previewProfileImage.visibility = View.VISIBLE
            fragment.binding.previewProfileImage.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    it,
                    0,
                    it.size
                )
            )
        }

        fragment.binding.uploadButton.setOnClickListener {
            Log.wtf("isi fragment profile picture", fragment.viewModel.profilePicture.value.toString() )
            if(fragment.viewModel.profilePicture.value?.isEmpty() == true){
                val chooserIntent = imageMediaService.createIntent()
                fragment.thumbnailChooserActivityLauncher.launch(chooserIntent)
            } else{
                val profilePicture = fragment.viewModel.profilePicture.value?: ByteArray(0)
                auth?.let { it1 -> userService.updateProfilePicture(profilePicture) }
            }
        }

        fragment.binding.updateButton.setOnClickListener {
            val username = fragment.binding.usernameEditText.text
            auth?.let { user ->
                SafetyNet.getClient(fragment.requireActivity())
                    .verifyWithRecaptcha("6Le5xrUaAAAAACrndJTA0nwjgx8S2_g0YJE07Nhg")
                    .addOnSuccessListener { response ->
                        if (response.tokenResult?.isNotEmpty() == true) {
                            userService.updateProfile(user.uid, username.toString())
                        }
                    }
                    .addOnFailureListener { e -> Log.wtf("hehe", e.toString()) }

            }
        }


    }

    fun onPreviewSelected(intent: Intent){
        intent.data?.let {
            fragment.viewModel.profilePicture.value = imageMediaService.byteArrayFromBitmap(
                imageMediaService.bitmapFromUri(
                    fragment.requireActivity().contentResolver,
                    it
                )
            )


            fragment.binding.uploadButton.text = "Save"
            fragment.binding.hintUpdateText.text = "Click here to Save it..."

            Snackbar.make(
                fragment.requireActivity().findViewById(R.id.coordinatorLayout),
                "Profile Picture Selected",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }



}
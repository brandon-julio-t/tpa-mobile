package edu.bluejack20_2.braven.pages.user_profile_edit

import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AuthenticationService
import edu.bluejack20_2.braven.services.ImageMediaService
import edu.bluejack20_2.braven.services.TimestampService
import javax.inject.Inject

class UserProfileEditController @Inject constructor(
    private val imageMediaService: ImageMediaService,
    private val authenticationService: AuthenticationService,
    private val userService: UserService,
    private val timestampService: TimestampService
) {

    private lateinit var fragment: UserProfileEditFragment
    private var auth: FirebaseUser? = null

    fun bind(fragment: UserProfileEditFragment) {
        this.fragment = fragment
        val viewModel = fragment.viewModel

        auth = authenticationService.getUser()
        auth?.let { userService.getUserById(it.uid).get().addOnSuccessListener {
            if (it != null) {

                val biography = it.get("biography")
                if(biography != null){
                    fragment.binding.biographyEditText.setText(biography.toString())
                }

                val dafeOfBirth = it.get("dateOfBirth")
                viewModel.dateOfBirthTimestamp = dafeOfBirth as Timestamp
                if(dafeOfBirth != null){
                    val date = (dafeOfBirth as? Timestamp)?.let { dob ->
                        timestampService.formatTimestamp(dob, TimestampService.PRETTY_LONG)
                    }
                    fragment.binding.dateOfBirthEditText.setText(date)
                }
            }
        } }
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
            if (fragment.viewModel.profilePicture.value?.isEmpty() == true) {
                val chooserIntent = imageMediaService.createIntent()
                fragment.thumbnailChooserActivityLauncher.launch(chooserIntent)
            } else {
                val profilePicture = fragment.viewModel.profilePicture.value ?: ByteArray(0)
                auth?.let { it1 -> userService.updateProfilePicture(profilePicture) }
            }
        }

        fragment.binding.dateOfBirthEditText.setOnClickListener {
            MaterialDatePicker.Builder.datePicker().build().let { picker ->
                picker.addOnPositiveButtonClickListener {
                    viewModel.dateOfBirthTimestamp = timestampService.millisecondsToTimestamp(it)
                }
                picker.show(fragment.requireActivity().supportFragmentManager, "dob-picker")
                picker.addOnPositiveButtonClickListener {
                    fragment.binding.dateOfBirthEditText.setText(
                        timestampService.formatMilliseconds(
                            it,
                            TimestampService.PRETTY_SHORT
                        )
                    )
                }
            }
        }

        fragment.binding.updateButton.setOnClickListener {

            val username = fragment.binding.usernameEditText.text.toString().trim(' ')
            val biography = fragment.binding.biographyEditText.text.toString().trim(' ')
            val password = fragment.binding.passwordEditText.text.toString()
            var user: DocumentSnapshot

            userService.getUserById(auth?.uid!!).get().addOnSuccessListener {
                user = it;

                if(username == ""){
                    Snackbar.make(
                        fragment.requireActivity().findViewById(R.id.coordinatorLayout),
                        fragment.getString(R.string.validator_username),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                else{
                    if(user.get("displayName") != username || user.get("biography") != biography || user.getTimestamp("dateOfBirth")?.seconds != viewModel.dateOfBirthTimestamp?.seconds){
                        auth?.let { user ->
                            SafetyNet.getClient(fragment.requireActivity())
                                .verifyWithRecaptcha("6Le5xrUaAAAAACrndJTA0nwjgx8S2_g0YJE07Nhg")
                                .addOnSuccessListener { response ->
                                    if (response.tokenResult?.isNotEmpty() == true) {
                                        userService.updateProfile(
                                            username,
                                            viewModel.dateOfBirthTimestamp,
                                            biography,
                                            password
                                        )
                                            ?.addOnSuccessListener {
                                                Snackbar.make(
                                                    fragment.requireActivity().findViewById(R.id.coordinatorLayout),
                                                    fragment.getString(R.string.sb_profile_updated),
                                                    Snackbar.LENGTH_LONG
                                                ).show()
                                            }
                                    }
                                }
                                .addOnFailureListener { e -> Log.wtf("hehe", e.toString()) }
                        }
                    }
                }

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


            fragment.binding.uploadButton.text = fragment.getString(R.string.save)
            fragment.binding.hintUpdateText.text = fragment.getString(R.string.click_save)

            Snackbar.make(
                fragment.requireActivity().findViewById(R.id.coordinatorLayout),
                fragment.getString(R.string.sb_profile_selected),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }



}
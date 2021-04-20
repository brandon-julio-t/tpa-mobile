package edu.bluejack20_2.braven.pages.user_profile_edit

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.View
import com.google.android.material.snackbar.Snackbar
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.services.ImageMediaService
import javax.inject.Inject

class UserProfileEditController @Inject constructor(
    private val imageMediaService: ImageMediaService
) {

    private lateinit var fragment: UserProfileEditFragment

    fun bind(fragment: UserProfileEditFragment){
        this.fragment = fragment

        fragment.viewModel.profilePicture.observe(fragment.viewLifecycleOwner){

            if(it.isEmpty()){
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
            val chooserIntent = imageMediaService.createIntent()
            fragment.thumbnailChooserActivityLauncher.launch(chooserIntent)
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

            Snackbar.make(
                fragment.requireActivity().findViewById(R.id.coordinatorLayout),
                "Profile Picture Selected",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }



}
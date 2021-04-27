package edu.bluejack20_2.braven.pages.user_profile_edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp

class UserProfileEditViewModel: ViewModel() {
    val profilePicture = MutableLiveData(ByteArray(0))
    var dateOfBirthTimestamp: Timestamp? = null
}
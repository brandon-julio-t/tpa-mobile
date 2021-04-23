package edu.bluejack20_2.braven.pages.user_profile_edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserProfileEditViewModel: ViewModel() {
    val profilePicture =  MutableLiveData(ByteArray(0))

    fun reset() {
        profilePicture.value = ByteArray(0)
    }
}
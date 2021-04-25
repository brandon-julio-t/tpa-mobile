package edu.bluejack20_2.braven.pages.post_edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PostEditViewModel : ViewModel() {
    val thumbnail: MutableLiveData<ByteArray> by lazy { MutableLiveData<ByteArray>(ByteArray(0)) }

}
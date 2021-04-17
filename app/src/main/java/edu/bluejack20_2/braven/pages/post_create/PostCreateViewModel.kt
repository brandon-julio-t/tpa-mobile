package edu.bluejack20_2.braven.pages.post_create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PostCreateViewModel : ViewModel() {
    val thumbnail: MutableLiveData<ByteArray> by lazy { MutableLiveData<ByteArray>(ByteArray(0)) }

    fun reset() {
        thumbnail.value = ByteArray(0)
    }
}
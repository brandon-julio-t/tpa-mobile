package edu.bluejack20_2.braven.pages.create_post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CreatePostViewModel : ViewModel() {
    val title: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val description: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val category: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val thumbnail: MutableLiveData<ByteArray> by lazy { MutableLiveData<ByteArray>() }
}
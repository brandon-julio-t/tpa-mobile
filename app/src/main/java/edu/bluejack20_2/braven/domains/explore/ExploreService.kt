package edu.bluejack20_2.braven.domains.explore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import javax.inject.Inject


class ExploreService @Inject constructor(){

    private val db = FirebaseFirestore.getInstance().collection("posts")
    private val storage = FirebaseStorage.getInstance().reference
    private val storageRoot = "thumbnails"

    fun getAllPosts() : Query = db.orderBy("timestamp", Query.Direction.DESCENDING)

    fun getStorageReference(id: String): StorageReference = storage.child("${storageRoot}/${id}")
    
}
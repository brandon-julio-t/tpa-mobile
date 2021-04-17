package edu.bluejack20_2.braven.domains.post

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import javax.inject.Inject

class PostRepository @Inject constructor() {
    private val db get() = FirebaseFirestore.getInstance().collection("posts")
    private val storage get() = FirebaseStorage.getInstance().reference
    private val storageRoot = "thumbnails"

    fun all(): Query = db.orderBy("timestamp", Query.Direction.DESCENDING)

    fun createWithoutThumbnail(data: HashMap<String, Any>): Task<DocumentReference> = db.add(data)

    fun createWithThumbnail(
        data: HashMap<String, Any>,
        thumbnail: Pair<String, ByteArray>
    ): Pair<Task<DocumentReference>, UploadTask> {
        val (thumbnailId, thumbnailData) = thumbnail

        val postTask = db.add(data)
        val thumbnailTask = storage.child("${storageRoot}/${thumbnailId}").putBytes(thumbnailData)

        return Pair(postTask, thumbnailTask)
    }
}
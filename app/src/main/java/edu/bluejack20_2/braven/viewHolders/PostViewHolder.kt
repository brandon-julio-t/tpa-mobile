package edu.bluejack20_2.braven.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack20_2.braven.databinding.ItemPostBinding
import edu.bluejack20_2.braven.modules.GlideApp

class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
    var id: String? = null

    fun bind(model: DocumentSnapshot) {
        val data = model.data

        val timestamp = data?.get("timestamp") as? Timestamp

        id = model.id
        binding.title.text = data?.get("title").toString()
        binding.createdAt.text = timestamp?.toDate().toString()
        binding.category.text = data?.get("category").toString()

        data?.get("thumbnailId")?.toString()?.let {
            val path = "thumbnails/${it}"
            val storageReference = FirebaseStorage.getInstance().reference.child(path)
            GlideApp.with(binding.root).load(storageReference).into(binding.thumbnail)
        }
    }
}
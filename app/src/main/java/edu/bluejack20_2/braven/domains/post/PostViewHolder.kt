package edu.bluejack20_2.braven.domains.post

import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack20_2.braven.databinding.ItemPostBinding
import edu.bluejack20_2.braven.modules.GlideApp
import edu.bluejack20_2.braven.pages.home.HomeFragmentDirections

class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model: DocumentSnapshot) {
        val data = model.data
        val timestamp = data?.get("timestamp") as? Timestamp

        data?.set("id", model.id)

        binding.title.text = data?.get("title").toString()
        binding.createdAt.text = timestamp?.toDate().toString()
        binding.category.text = data?.get("category").toString()
        binding.cardLayout.setOnClickListener {
            binding.root.findNavController()
                .navigate(HomeFragmentDirections.toPostDetail(bundleOf("post" to data)))
        }

        val path = "thumbnails/${model.id}"
        val storageReference = FirebaseStorage.getInstance().reference.child(path)
        GlideApp.with(binding.root).load(storageReference).into(binding.thumbnail)
    }
}
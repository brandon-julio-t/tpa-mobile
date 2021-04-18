package edu.bluejack20_2.braven.domains.explore

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack20_2.braven.databinding.ItemExploreBinding
import edu.bluejack20_2.braven.modules.GlideApp
import edu.bluejack20_2.braven.services.AuthenticationService


class ExploreViewHolder(
    private val binding: ItemExploreBinding,
    private val authenticationService: AuthenticationService
):RecyclerView.ViewHolder(binding.root) {

    fun bind(explore: DocumentSnapshot){
        Log.wtf("test", explore.data.toString())
        explore.data?.let{
            posts ->

            val timestamp = posts["timestamp"] as? Timestamp

            posts["id"] = explore.id

            authenticationService.getUserById(posts["userId"].toString()).addOnSuccessListener {
                it.data?.let { user ->
                    binding.usernameText.text = user["displayName"].toString()

                    user["photoUrl"]?.let { url ->
                        Glide.with(binding.root)
                            .load(url.toString())
                            .into(binding.profilePictureImage)
                    }
                }
            }

            binding.createdAtText.text = timestamp?.toDate().toString()
            binding.titlePostText.text = posts["title"].toString()
            binding.categoryText.text = posts["category"].toString()
            binding.contentPostText.text = posts["description"].toString()

            val path = "thumbnails/${explore.id}"
            val storageReference = FirebaseStorage.getInstance().reference.child(path)
            GlideApp.with(binding.root)
                .load(storageReference)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.thumbnailImage.layoutParams.height = 0
                        binding.thumbnailImage.requestLayout()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .into(binding.thumbnailImage)

        }
    }

}
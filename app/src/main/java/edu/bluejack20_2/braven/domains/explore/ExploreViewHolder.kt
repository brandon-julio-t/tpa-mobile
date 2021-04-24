package edu.bluejack20_2.braven.domains.explore

import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.modules.GlideApp
import edu.bluejack20_2.braven.pages.explore.ExploreFragmentDirections
import edu.bluejack20_2.braven.services.TimestampService

class ExploreViewHolder(
    private val binding: ItemExploreBinding,
    private val userService: UserService,
    private val fragment: Fragment,
    private val timestampService: TimestampService
):RecyclerView.ViewHolder(binding.root) {
    fun bind(explore: DocumentSnapshot){
        explore.data?.let{
            posts ->

            val timestamp = posts["timestamp"] as? Timestamp

            posts["id"] = explore.id

            userService.getUserById(posts["userId"].toString()).get().addOnSuccessListener {
                it.data?.let { user ->
                    binding.usernameText.text = user["displayName"].toString()

                    user["photoUrl"]?.let { url ->
                        Glide.with(binding.root)
                            .load(url.toString())
                            .into(binding.profilePictureImage)
                    }
                }
            }

            binding.createdAtText.text = timestamp?.let { timestampService.prettyTime(it) }
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

            binding.cardLayout.setOnClickListener {
                fragment.findNavController().navigate(
                    ExploreFragmentDirections.exploreToPostDetail(posts["id"].toString())
                )
            }

        }
    }
}
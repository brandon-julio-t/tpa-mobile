package edu.bluejack20_2.braven.pages.user_profile.view_pager_fragments.recent_likes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentReference
import edu.bluejack20_2.braven.databinding.ItemPostBinding
import edu.bluejack20_2.braven.domains.post.PostViewHolderModule

class RecentLikesAdapter(
    private val fragment: RecentLikesFragment,
    private val posts: List<DocumentReference>,
    private val postViewHolderModule: PostViewHolderModule
) : RecyclerView.Adapter<PostViewHolderModule.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        postViewHolderModule.ViewHolder(
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            fragment,
        )

    override fun onBindViewHolder(holder: PostViewHolderModule.ViewHolder, position: Int) {
        posts[position].get().addOnSuccessListener { holder.bind(it) }
    }

    override fun getItemCount(): Int = posts.size
}
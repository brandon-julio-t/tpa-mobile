package edu.bluejack20_2.braven.pages.user_profile.view_pager_fragments.recent_likes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentReference
import edu.bluejack20_2.braven.databinding.ItemPostBinding
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.domains.post.PostViewHolder
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AuthenticationService

class RecentLikesAdapter(
    private val fragment: RecentLikesFragment,
    private val userService: UserService,
    private val postService: PostService,
    private val posts: List<DocumentReference>,
    private val authenticationService: AuthenticationService,
    private val notificationService: NotificationService
) :
    RecyclerView.Adapter<PostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder =
        PostViewHolder(
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            fragment,
            userService,
            postService,
            authenticationService,
            notificationService
        )

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        posts[position].get().addOnSuccessListener { holder.bind(it) }
    }

    override fun getItemCount(): Int = posts.size
}
package edu.bluejack20_2.braven.domains.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.databinding.ItemPostBinding
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AuthenticationService

class PostFirestorePagingAdapter(
    private val fragment: Fragment,
    private val userService: UserService,
    private val postService: PostService,
    options: FirestorePagingOptions<DocumentSnapshot>
) :
    FirestorePagingAdapter<DocumentSnapshot, PostViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder =
        PostViewHolder(
            ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            fragment,
            userService,
            postService
        )

    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int,
        model: DocumentSnapshot
    ) = holder.bind(model)
}
package edu.bluejack20_2.braven.domains.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.databinding.ItemCommentBinding
import edu.bluejack20_2.braven.domains.user.UserService

class CommentFirestorePagingAdapter(
    private val fragment: Fragment,
    private val userService: UserService,
    options: FirestorePagingOptions<DocumentSnapshot>
) :
    FirestorePagingAdapter<DocumentSnapshot, CommentViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder =
        CommentViewHolder(
            ItemCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            userService
        )

    override fun onBindViewHolder(
        holder: CommentViewHolder,
        position: Int,
        model: DocumentSnapshot
    ) = holder.bind(fragment, model)
}
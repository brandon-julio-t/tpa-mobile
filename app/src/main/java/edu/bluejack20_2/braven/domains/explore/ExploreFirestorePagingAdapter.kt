package edu.bluejack20_2.braven.domains.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.databinding.ItemExploreBinding
import edu.bluejack20_2.braven.domains.comment.CommentViewHolder
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AuthenticationService

class ExploreFirestorePagingAdapter (
    private val fragment: Fragment,
    private val userService: UserService,
    options: FirestorePagingOptions<DocumentSnapshot>
        ): FirestorePagingAdapter<DocumentSnapshot, ExploreViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreViewHolder =
        ExploreViewHolder(
            ItemExploreBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            userService,
            fragment
        )

    override fun onBindViewHolder(
        holder: ExploreViewHolder,
        position: Int,
        model: DocumentSnapshot
    ) = holder.bind(model)
}
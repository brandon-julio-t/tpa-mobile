package edu.bluejack20_2.braven.domains.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import edu.bluejack20_2.braven.databinding.ItemCommentBinding
import edu.bluejack20_2.braven.factories.FirestorePagingAdapterOptionsFactory
import javax.inject.Inject

class CommentFirestorePagingAdapterModule @Inject constructor(private val commentViewHolderModule: CommentViewHolderModule) {
    inner class Adapter(private val fragment: Fragment, query: Query) :
        FirestorePagingAdapter<DocumentSnapshot, CommentViewHolderModule.ViewHolder>(
            FirestorePagingAdapterOptionsFactory(fragment.viewLifecycleOwner, query).create()
        ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            commentViewHolderModule.ViewHolder(
                ItemCommentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                fragment
            )

        override fun onBindViewHolder(
            holder: CommentViewHolderModule.ViewHolder,
            position: Int,
            model: DocumentSnapshot
        ) = holder.bind(model)
    }
}
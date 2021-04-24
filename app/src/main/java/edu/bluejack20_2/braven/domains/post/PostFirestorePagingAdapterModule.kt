package edu.bluejack20_2.braven.domains.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import edu.bluejack20_2.braven.databinding.ItemPostBinding
import edu.bluejack20_2.braven.factories.FirestorePagingAdapterOptionsFactory
import javax.inject.Inject

class PostFirestorePagingAdapterModule @Inject constructor(private val postViewHolderModule: PostViewHolderModule) {
    inner class Adapter(private val fragment: Fragment, query: Query) :
        FirestorePagingAdapter<DocumentSnapshot, PostViewHolderModule.ViewHolder>(
            FirestorePagingAdapterOptionsFactory(fragment.viewLifecycleOwner, query).create()
        ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            postViewHolderModule.ViewHolder(
                ItemPostBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                fragment
            )

        override fun onBindViewHolder(
            holder: PostViewHolderModule.ViewHolder,
            position: Int,
            model: DocumentSnapshot
        ) = holder.bind(model)
    }
}

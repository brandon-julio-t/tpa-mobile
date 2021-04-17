package edu.bluejack20_2.braven.domains.post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.databinding.ItemPostBinding
import edu.bluejack20_2.braven.viewHolders.PostViewHolder

class PostFirestorePagingAdapter(
    private val fragment: Fragment,
    private val progressIndicator: View,
    options: FirestorePagingOptions<DocumentSnapshot>
) :
    FirestorePagingAdapter<DocumentSnapshot, PostViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder =
        PostViewHolder(
            ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int,
        model: DocumentSnapshot
    ) = holder.bind(model)

    override fun onLoadingStateChanged(state: LoadingState) {
        super.onLoadingStateChanged(state)

        progressIndicator.visibility = when (state) {
            LoadingState.LOADING_INITIAL, LoadingState.LOADED, LoadingState.FINISHED -> View.INVISIBLE
            LoadingState.LOADING_MORE -> View.VISIBLE
            LoadingState.ERROR -> {
                Snackbar.make(
                    fragment.requireActivity().findViewById(R.id.coordinatorLayout),
                    "Infinite Scrolling Error",
                    Snackbar.LENGTH_LONG
                ).show()

                View.GONE
            }
        }
    }
}
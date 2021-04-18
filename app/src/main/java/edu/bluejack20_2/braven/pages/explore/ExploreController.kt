package edu.bluejack20_2.braven.pages.explore

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import edu.bluejack20_2.braven.domains.explore.ExploreFirestorePagingAdapter
import edu.bluejack20_2.braven.domains.explore.ExploreService
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.factories.FirestorePagingAdapterOptionFactory
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

class ExploreController @Inject constructor(
    private val exploreService: ExploreService,
    private val authenticationService: AuthenticationService
) {

    fun bind(fragment: ExploreFragment){
        val binding = fragment.binding

        binding.exploreRecycleview.layoutManager = LinearLayoutManager(
            fragment.requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )

        binding.exploreRecycleview.adapter = ExploreFirestorePagingAdapter(fragment, authenticationService,
            FirestorePagingAdapterOptionFactory(fragment, exploreService.getAllPosts()).create()
        )
    }

}
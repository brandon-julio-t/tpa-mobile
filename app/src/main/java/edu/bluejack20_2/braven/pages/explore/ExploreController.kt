package edu.bluejack20_2.braven.pages.explore

import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack20_2.braven.domains.explore.ExploreFirestorePagingAdapter
import edu.bluejack20_2.braven.domains.explore.ExploreService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.factories.FirestorePagingAdapterOptionsFactory
import javax.inject.Inject

class ExploreController @Inject constructor(
    private val exploreService: ExploreService,
    private val userService: UserService
) {

    fun bind(fragment: ExploreFragment){
        val binding = fragment.binding

        binding.exploreRecycleview.layoutManager = LinearLayoutManager(
            fragment.requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )

        binding.exploreRecycleview.adapter = ExploreFirestorePagingAdapter(fragment, userService,
            FirestorePagingAdapterOptionsFactory(fragment, exploreService.getAllPosts()).create()
        )
    }

}
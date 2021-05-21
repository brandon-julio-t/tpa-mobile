package edu.bluejack20_2.braven.pages.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.databinding.ItemPostBinding
import edu.bluejack20_2.braven.domains.post.PostViewHolderModule
import javax.inject.Inject

class HomeController @Inject constructor(
    private val postViewHolderModule: PostViewHolderModule
) {
    private val posts = mutableListOf<DocumentSnapshot>()

    fun bind(fragment: HomeFragment) {
        val binding = fragment.binding
        val viewModel = fragment.viewModel

        binding.posts.setLayoutManager(LinearLayoutManager(fragment.requireActivity()))

        binding.posts.adapter = object :
            RecyclerView.Adapter<PostViewHolderModule.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ) = postViewHolderModule.ViewHolder(
                ItemPostBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                fragment
            )

            override fun onBindViewHolder(
                holder: PostViewHolderModule.ViewHolder,
                position: Int
            ) = holder.bind(posts[position])

            override fun getItemCount() = posts.size
        }

        binding.posts.setRefreshListener { viewModel.refresh() }

        binding.posts.setupMoreListener({ overallItemsCount, itemsBeforeMore, maxLastVisiblePosition ->
            val data = viewModel.posts.value ?: emptyList()
            var counter = 0

            for (i in maxLastVisiblePosition until data.size) {
                posts.add(data[i])

                counter++
                if (counter >= 10) break
            }
        }, 10)

        viewModel.posts.observe(fragment.viewLifecycleOwner) {
            posts.clear()

            var counter = 0

            for (element in it) {
                posts.add(element)

                counter++
                if (counter >= 10) break
            }

            binding.posts.adapter.notifyDataSetChanged()
        }
    }
}
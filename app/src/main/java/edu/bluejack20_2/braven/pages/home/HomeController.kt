package edu.bluejack20_2.braven.pages.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.braven.databinding.ItemPostBinding
import edu.bluejack20_2.braven.domains.post.PostFirestorePagingAdapterModule
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.domains.post.PostViewHolderModule
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

class HomeController @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val postService: PostService,
    private val userService: UserService,
    private val postFirestorePagingAdapterModule: PostFirestorePagingAdapterModule,
    private val postViewHolderModule: PostViewHolderModule
) {
    private lateinit var adapter: RecyclerView.Adapter<PostViewHolderModule.ViewHolder>

    fun bind(fragment: HomeFragment) {
        val binding = fragment.binding

//        binding.posts.setRefreshListener { adapter.refresh() }

        binding.posts.setLayoutManager(LinearLayoutManager(fragment.requireActivity()))

        authenticationService.getUser()?.let { auth ->
            userService.getUserById(auth.uid).get().addOnSuccessListener { user ->
                val followings = user.get("followings").let {
                    val default = listOf(authenticationService.getUser()?.uid.toString())

                    var list = (it as? List<*>)
                    list = list?.mapNotNull { e -> e as? String }
                    list = list?.union(default)?.toList()
                    list ?: emptyList()
                }

                postService.getAllFollowingsPosts(user).get().addOnSuccessListener { query ->
                    val posts = query.documents.filter { followings.contains(it.data?.get("userId")) }

                    adapter = object:
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

                    binding.posts.adapter = adapter
                }

//                adapter = postFirestorePagingAdapterModule.Adapter(
//                    fragment,
//                    postService.getAllFollowingsPosts(user)
//                )
//
//                binding.posts.adapter = adapter
            }
        }
    }
}
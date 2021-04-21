package edu.bluejack20_2.braven.pages.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.braven.databinding.FragmentHomeBinding
import edu.bluejack20_2.braven.databinding.ItemPostBinding
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.domains.post.PostViewHolder
import edu.bluejack20_2.braven.domains.user.UserService
import javax.inject.Inject

class HomeController @Inject constructor(
    private val postService: PostService,
    private val userService: UserService
) {
    private lateinit var fragment: HomeFragment
    private lateinit var binding: FragmentHomeBinding

    fun bind(fragment: HomeFragment) {
        this.fragment = fragment
        binding = fragment.binding

        binding.createPost.setOnClickListener {
            fragment.findNavController().navigate(HomeFragmentDirections.homeToPostCreate())
        }

        setupPostsSuperRecyclerView()
    }

    private fun setupPostsSuperRecyclerView() {
        binding.posts.layoutManager = LinearLayoutManager(
            fragment.requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )

        binding.posts.adapter = object : RecyclerView.Adapter<PostViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder =
                PostViewHolder(
                    ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                    fragment,
                    userService,
                    postService
                )

            override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
                fragment.viewModel.posts.value?.get(position)?.let { holder.bind(it) }
            }

            override fun getItemCount(): Int = fragment.viewModel.posts.value?.size ?: 0
        }

        fragment.viewModel.posts.observe(fragment.viewLifecycleOwner, {
            binding.posts.adapter?.notifyDataSetChanged()
        })
    }
}
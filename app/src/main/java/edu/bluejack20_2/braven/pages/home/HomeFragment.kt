package edu.bluejack20_2.braven.pages.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.SnapshotParser
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.firebase.ui.firestore.paging.LoadingState.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack20_2.braven.R
import edu.bluejack20_2.braven.databinding.FragmentHomeBinding
import edu.bluejack20_2.braven.databinding.ItemPostBinding
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.viewHolders.PostViewHolder
import kotlinx.android.synthetic.main.activity_main.view.*
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var postService: PostService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val baseQuery = postService.getAllPosts()

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(2)
            .setPageSize(4)
            .build()

        val options = FirestorePagingOptions.Builder<DocumentSnapshot>()
            .setLifecycleOwner(this)
            .setQuery(baseQuery, config, SnapshotParser { return@SnapshotParser it })
            .build()

        val adapter = object : FirestorePagingAdapter<DocumentSnapshot, PostViewHolder>(options) {
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

                binding.progressIndicator.visibility = when (state) {
                    LOADING_INITIAL, LOADED, FINISHED -> View.GONE
                    LOADING_MORE -> View.VISIBLE
                    ERROR -> {
                        Snackbar.make(
                            requireActivity().findViewById(R.id.coordinatorLayout),
                            "Infinite Scrolling Error",
                            Snackbar.LENGTH_LONG
                        ).show()

                        View.GONE
                    }
                }
            }
        }

        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.posts.layoutManager = layoutManager

        binding.posts.addItemDecoration(
            DividerItemDecoration(
                binding.posts.context,
                layoutManager.orientation
            )
        )

        binding.posts.adapter = adapter

        binding.createPost.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.toCreatePost())
        }
    }
}
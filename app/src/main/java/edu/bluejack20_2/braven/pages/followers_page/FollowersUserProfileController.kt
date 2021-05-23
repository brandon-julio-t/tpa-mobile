package edu.bluejack20_2.braven.pages.followers_page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.databinding.FragmentFollowersUserProfileBinding
import edu.bluejack20_2.braven.databinding.ItemFollowersBinding
import edu.bluejack20_2.braven.domains.followers.FollowersUserViewHolderModule
import javax.inject.Inject

class FollowersUserProfileController @Inject constructor(
    private val followersUserViewHolderModule: FollowersUserViewHolderModule,
) {
    private lateinit var binding: FragmentFollowersUserProfileBinding
    private lateinit var viewModel: FollowersUserProfileViewModel

    private var followers = mutableListOf<DocumentSnapshot>()

    fun bind(fragment: FollowersUserProfileFragment) {
        this.binding = fragment.binding
        this.viewModel = fragment.viewModel

        viewModel.refresh(fragment)
        handleViewModel(fragment)
        handleUi(fragment)
    }

    private fun handleViewModel(fragment: FollowersUserProfileFragment) {
        binding.viewModel = viewModel

        viewModel.username.observe(fragment.viewLifecycleOwner, {
            viewModel.beginSearch()
        })

        viewModel.users.observe(fragment.viewLifecycleOwner, {
//            /* SHOW ERROR MESSAGE

            if (it.isEmpty()) {
                binding.followersUserRecycleview.visibility = View.GONE
                binding.errorMessageText.visibility = View.VISIBLE
            } else {
                binding.followersUserRecycleview.visibility = View.VISIBLE
                binding.errorMessageText.visibility = View.GONE
            }

//            */

            followers.clear()
            followers.addAll(it)

//            val data = viewModel.users.value ?: emptyList()
//            var counter = 0
//
//            for (element in data) {
//                followers.add(element)
//
//                counter++
//                if (counter >= 10) break
//            }
//
//            val distinct = followers.distinctBy { it.id }
//            followers.clear()
//            followers.addAll(distinct)

            binding.followersUserRecycleview.adapter.notifyDataSetChanged()
        })
    }

    private fun handleUi(fragment: FollowersUserProfileFragment) {
        binding.followersUserRecycleview.setLayoutManager(LinearLayoutManager(fragment.requireActivity()))

        binding.followersUserRecycleview.adapter =
            object : RecyclerView.Adapter<FollowersUserViewHolderModule.ViewHolder>() {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): FollowersUserViewHolderModule.ViewHolder =
                    followersUserViewHolderModule.ViewHolder(
                        ItemFollowersBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                        ),
                        fragment,
                        fragment.userId.toString()
                    )

                override fun onBindViewHolder(
                    holder: FollowersUserViewHolderModule.ViewHolder,
                    position: Int
                ) = holder.bind(followers[position])

                override fun getItemCount(): Int = followers.size
            }

//        binding.followersUserRecycleview.setupMoreListener({ _, _, start ->
//            val data = viewModel.users.value ?: emptyList()
//            var counter = 0
//
//            for (i in start until data.size) {
//                followers.add(data[i])
//
//                counter++
//                if (counter >= 10) break
//            }
//
//            val distinct = followers.distinctBy { it.id }
//            followers.clear()
//            followers.addAll(distinct)
//        }, 10)
    }
}
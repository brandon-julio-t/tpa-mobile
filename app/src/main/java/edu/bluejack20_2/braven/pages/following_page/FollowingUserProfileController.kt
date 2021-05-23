package edu.bluejack20_2.braven.pages.following_page

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack20_2.braven.databinding.FragmentFollowingUserProfileBinding
import edu.bluejack20_2.braven.databinding.ItemFollowingBinding
import edu.bluejack20_2.braven.domains.following.FollowingUserViewHolderModule
import javax.inject.Inject

class FollowingUserProfileController @Inject constructor(
    private val followingUserViewHolderModule: FollowingUserViewHolderModule,
) {
    private lateinit var binding: FragmentFollowingUserProfileBinding
    private lateinit var viewModel: FollowingUserProfileViewModel

    private var followings = mutableListOf<DocumentSnapshot>()

    fun bind(fragment: FollowingUserProfileFragment) {
        this.binding = fragment.binding
        this.viewModel = fragment.viewModel

        binding.errorMessageText.visibility = View.GONE

        viewModel.refresh(fragment)
        handleViewModel(fragment)
        handleUi(fragment)
    }

    private fun handleViewModel(fragment: FollowingUserProfileFragment) {
        binding.viewModel = viewModel

        viewModel.username.observe(fragment.viewLifecycleOwner, {
            viewModel.beginSearch()
        })

        viewModel.users.observe(fragment.viewLifecycleOwner, {
//            /* SHOW ERROR MESSAGE

            if (it.isEmpty()) {
                binding.followingUserRecycleview.visibility = View.GONE
                binding.errorMessageText.visibility = View.VISIBLE
            } else {
                binding.followingUserRecycleview.visibility = View.VISIBLE
                binding.errorMessageText.visibility = View.GONE
            }

//            */

            followings.clear()

            var counter = 0

            for (element in it) {
                followings.add(element)

                counter++
                if (counter >= 10) break
            }

            binding.followingUserRecycleview.adapter.notifyDataSetChanged()
        })
    }

    private fun handleUi(fragment: FollowingUserProfileFragment) {
        binding.followingUserRecycleview.setLayoutManager(LinearLayoutManager(fragment.requireActivity()))

        binding.followingUserRecycleview.adapter =
            object : RecyclerView.Adapter<FollowingUserViewHolderModule.ViewHolder>() {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): FollowingUserViewHolderModule.ViewHolder =
                    followingUserViewHolderModule.ViewHolder(
                        ItemFollowingBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                        ),
                        fragment,
                        fragment.userId.toString()
                    )

                override fun onBindViewHolder(
                    holder: FollowingUserViewHolderModule.ViewHolder,
                    position: Int
                ) = holder.bind(followings[position])

                override fun getItemCount(): Int = followings.size
            }

        binding.followingUserRecycleview.setupMoreListener({ _, _, start ->
            val data = viewModel.users.value ?: emptyList()
            var counter = 0

            for (i in start until data.size) {
                followings.add(data[i])

                counter++
                if (counter >= 10) break
            }

            Log.wtf("test", "infinite");

            val distinct = followings.distinctBy { it.id }
            followings.clear()
            followings.addAll(distinct)
        }, 10)
    }
}
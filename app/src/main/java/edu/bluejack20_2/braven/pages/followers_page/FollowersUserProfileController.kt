package edu.bluejack20_2.braven.pages.followers_page

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import edu.bluejack20_2.braven.databinding.FragmentFollowersUserProfileBinding
import edu.bluejack20_2.braven.databinding.ItemFollowersBinding
import edu.bluejack20_2.braven.databinding.ItemPostBinding
import edu.bluejack20_2.braven.domains.followers.FollowersFirestorePagingAdapter
import edu.bluejack20_2.braven.domains.followers.FollowersUserService
import edu.bluejack20_2.braven.domains.followers.FollowersUserViewHolder
import edu.bluejack20_2.braven.domains.followers.FollowersUserViewHolderModule
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.post.PostViewHolderModule
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.factories.FirestorePagingAdapterOptionsFactory
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

class FollowersUserProfileController @Inject constructor(
    private val followersUserViewHolderModule: FollowersUserViewHolderModule,
    private val followersUserService: FollowersUserService,
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val notificationService: NotificationService
) {

    private lateinit var binding: FragmentFollowersUserProfileBinding
    private lateinit var viewModel: FollowersUserProfileViewModel

    fun bind(fragment: FollowersUserProfileFragment){
        this.binding = fragment.binding
        this.viewModel = fragment.viewModel

        viewModel.refresh(fragment)
        handleViewModel(fragment)
        handleUi(fragment)
    }

    fun handleViewModel(fragment: FollowersUserProfileFragment){
        binding.viewModel = viewModel

        viewModel.username.observe(fragment.viewLifecycleOwner, {
            viewModel.beginSearch()
        })

        viewModel.users.observe(fragment.viewLifecycleOwner, {
//            /* SHOW ERROR MESSAGE

             if(it.size == 0){
                binding.followersUserRecycleview.visibility = View.GONE
                binding.errorMessageText.visibility = View.VISIBLE
            }
            else{
                binding.followersUserRecycleview.visibility = View.VISIBLE
                binding.errorMessageText.visibility = View.GONE
            }

//            */

            binding.followersUserRecycleview.adapter?.notifyDataSetChanged()
        })
    }

    fun handleUi(fragment: FollowersUserProfileFragment){
        binding.followersUserRecycleview.layoutManager = LinearLayoutManager(fragment.requireActivity())

        binding.followersUserRecycleview.adapter = object : RecyclerView.Adapter<FollowersUserViewHolderModule.ViewHolder>(){
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): FollowersUserViewHolderModule.ViewHolder =
                followersUserViewHolderModule.ViewHolder(
                    ItemFollowersBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                    fragment,
                    fragment.userId.toString()
                )

            override fun onBindViewHolder(
                holder: FollowersUserViewHolderModule.ViewHolder,
                position: Int
            ) {
                viewModel.users.value?.get(position)?.let { holder.bind(it) }
            }

            override fun getItemCount(): Int = viewModel.users.value?.size ?:0

        }
    }


    /*
    fun bind(fragment: FollowersUserProfileFragment){
        val binding = fragment.binding
        val user = authenticationService.getUser()

        val loginId = fragment.requireArguments().getString("auth")

        userService.getUserById(loginId.toString()).get().addOnSuccessListener {
            val followers = it.get("followers") as List<String>

            val query = followersUserService.getAllUserFollowers(followers)

            binding.followersUserRecycleview.layoutManager = LinearLayoutManager(
                fragment.requireActivity(),
                LinearLayoutManager.VERTICAL,
                false
            )

            binding.followersUserRecycleview.adapter = FollowersFirestorePagingAdapter(
                followersUserService,
                userService,
                authenticationService,
                loginId!!,
                notificationService,
                FirestorePagingAdapterOptionsFactory(fragment, query!!).create()
            )
        }

    }

    */


}
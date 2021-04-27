package edu.bluejack20_2.braven.pages.following_page

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.braven.databinding.FragmentFollowersUserProfileBinding
import edu.bluejack20_2.braven.databinding.FragmentFollowingUserProfileBinding
import edu.bluejack20_2.braven.databinding.ItemFollowersBinding
import edu.bluejack20_2.braven.databinding.ItemFollowingBinding
import edu.bluejack20_2.braven.domains.followers.FollowersFirestorePagingAdapter
import edu.bluejack20_2.braven.domains.followers.FollowersUserViewHolderModule
import edu.bluejack20_2.braven.domains.following.FollowingFirestorePagingAdapter
import edu.bluejack20_2.braven.domains.following.FollowingUserService
import edu.bluejack20_2.braven.domains.following.FollowingUserViewHolderModule
import edu.bluejack20_2.braven.domains.notification.NotificationService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.factories.FirestorePagingAdapterOptionsFactory
import edu.bluejack20_2.braven.pages.followers_page.FollowersUserProfileFragment
import edu.bluejack20_2.braven.pages.followers_page.FollowersUserProfileViewModel
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

class FollowingUserProfileController @Inject constructor(
    private val followingUserViewHolderModule: FollowingUserViewHolderModule,
    private val followingUserService: FollowingUserService,
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val notificationService: NotificationService
) {

    private lateinit var binding: FragmentFollowingUserProfileBinding
    private lateinit var viewModel: FollowingUserProfileViewModel

    fun bind(fragment: FollowingUserProfileFragment){
        this.binding = fragment.binding
        this.viewModel = fragment.viewModel

        binding.errorMessageText.visibility = View.GONE

        viewModel.refresh(fragment)
        handleViewModel(fragment)
        handleUi(fragment)
    }

    fun handleViewModel(fragment: FollowingUserProfileFragment){
        binding.viewModel = viewModel

        viewModel.username.observe(fragment.viewLifecycleOwner, {
            viewModel.beginSearch()
        })

        viewModel.users.observe(fragment.viewLifecycleOwner, {
//            /* SHOW ERROR MESSAGE

            if(it.size == 0){
                binding.followingUserRecycleview.visibility = View.GONE
                binding.errorMessageText.visibility = View.VISIBLE
            }
            else{
                binding.followingUserRecycleview.visibility = View.VISIBLE
                binding.errorMessageText.visibility = View.GONE
            }

//            */

            binding.followingUserRecycleview.adapter?.notifyDataSetChanged()
        })
    }

    fun handleUi(fragment: FollowingUserProfileFragment){
        binding.followingUserRecycleview.layoutManager = LinearLayoutManager(fragment.requireActivity())

        binding.followingUserRecycleview.adapter = object : RecyclerView.Adapter<FollowingUserViewHolderModule.ViewHolder>(){
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): FollowingUserViewHolderModule.ViewHolder =
                followingUserViewHolderModule.ViewHolder(
                    ItemFollowingBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                    fragment,
                    fragment.userId.toString()
                )

            override fun onBindViewHolder(
                holder: FollowingUserViewHolderModule.ViewHolder,
                position: Int
            ) {
                viewModel.users.value?.get(position)?.let { holder.bind(it) }
            }

            override fun getItemCount(): Int = viewModel.users.value?.size ?:0

        }
    }



    /*

    private fun initRecycleView(binding: FragmentFollowingUserProfileBinding, fragment: FollowingUserProfileFragment){

        val binding = fragment.binding
        val user = authenticationService.getUser()
        val loginId = fragment.requireArguments().getString("auth")

        binding.followingUserRecycleview.layoutManager = LinearLayoutManager(
            fragment.requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )

        userService.getUserById(loginId.toString()).get().addOnSuccessListener {


            var followings = it.get("followings") as List<String>

            followings = followings.toMutableList().also {
                it.add("mrfkejgnejgnrjfnwmekmfkwa")
            }

            val query = followingUserService.getAllUserFollowing(followings)

            binding.followingUserRecycleview.layoutManager = LinearLayoutManager(
                fragment.requireActivity(),
                LinearLayoutManager.VERTICAL,
                false
            )

            binding.followingUserRecycleview.adapter = FollowingFirestorePagingAdapter(
                followings,
                followingUserService,
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
package edu.bluejack20_2.braven.pages.following_page

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.bluejack20_2.braven.domains.followers.FollowersUserService
import edu.bluejack20_2.braven.domains.following.FollowingUserService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.pages.followers_page.FollowersUserProfileFragment
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

@HiltViewModel
class FollowingUserProfileViewModel @Inject constructor(
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val followingUserService: FollowingUserService
): ViewModel(){

    private val _originalUsers = MutableLiveData<List<DocumentSnapshot>>()
    private val _users =  MutableLiveData<List<DocumentSnapshot>>()
    val users: LiveData<List<DocumentSnapshot>> = _users
    val username = MutableLiveData("")

    fun beginSearch(){
        _users.value = _originalUsers.value?.filter { doc ->
            val hasUsername = doc.getString("displayName")
                ?.contains(username.value.toString(), true) == true
            hasUsername
        } ?: emptyList()
    }


    fun reset(){
        username.value = ""
    }

    fun refresh(fragment: FollowingUserProfileFragment) {
        val loginId = fragment.requireArguments().getString("auth")
        userService.getUserById(loginId.toString()).get().addOnSuccessListener {
            if(!(it.get("followings") as List<String>).isEmpty()){
                val followings = it.get("followings") as List<String>

                val query = followingUserService.getAllUserFollowing(followings)

                query.get().addOnSuccessListener {
                    _originalUsers.value = it.documents
                    _users.value = it.documents
                    Log.wtf("size", it.size().toString())
                }
            }

        }
    }


}
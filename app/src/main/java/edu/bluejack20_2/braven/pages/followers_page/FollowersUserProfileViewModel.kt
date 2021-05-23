package edu.bluejack20_2.braven.pages.followers_page

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.bluejack20_2.braven.domains.followers.FollowersUserService
import edu.bluejack20_2.braven.domains.user.UserService
import javax.inject.Inject

@HiltViewModel
class FollowersUserProfileViewModel @Inject constructor(
    private val userService: UserService,
    private val followersUserService: FollowersUserService
): ViewModel(){
    private val _originalUsers = MutableLiveData<List<DocumentSnapshot>>()
    private val _users =  MutableLiveData<List<DocumentSnapshot>>()

    val users: LiveData<List<DocumentSnapshot>> = _users
    val username = MutableLiveData("")

    fun beginSearch(){
        _users.value = _originalUsers.value?.filter { doc ->
            doc.getString("displayName")?.contains(username.value.toString(), true) == true
        } ?: emptyList()
    }

    fun reset(){
        username.value = ""
    }

    fun refresh(fragment: FollowersUserProfileFragment) {
        val loginId = fragment.requireArguments().getString("auth")
        userService.getUserById(loginId.toString()).get().addOnSuccessListener {
            if(!(it.get("followers") as List<String>).isEmpty()){
                val followers = it.get("followers") as List<String>

                _originalUsers.value = listOf()

                followersUserService.getAllUserFollowers(followers).forEach { query ->
                    query.get().addOnSuccessListener { doc ->
                        _originalUsers.value = _originalUsers.value?.plus(doc) ?: listOf(doc)
//                        _users.value = _users.value?.plus(doc) ?: listOf(doc)
                        _users.value = _originalUsers.value
                    }
                }
            }

        }
    }
}
package edu.bluejack20_2.braven.pages.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.domains.user.UserService
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val userService: UserService,
    private val postService: PostService,
) : ViewModel() {
    private val _posts: MutableLiveData<List<DocumentSnapshot>> by lazy {
        MutableLiveData<List<DocumentSnapshot>>().also { refresh() }
    }

    val posts: LiveData<List<DocumentSnapshot>> = _posts

    fun refresh() {
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
                    _posts.value = query.documents.filter { followings.contains(it.data?.get("userId")) }
                }
            }
        }
    }
}
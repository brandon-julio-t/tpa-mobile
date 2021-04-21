package edu.bluejack20_2.braven.pages.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.bluejack20_2.braven.domains.followers.FollowersUserService
import edu.bluejack20_2.braven.domains.post.PostService
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val postService: PostService,
    private val userService: FollowersUserService
) : ViewModel() {
    private val _posts: MutableLiveData<List<DocumentSnapshot>> by lazy {
        MutableLiveData<List<DocumentSnapshot>>(emptyList()).also { refresh() }
    }

    val posts: LiveData<List<DocumentSnapshot>> get() = _posts

    private fun refresh() {
        authenticationService.getUser()?.let { auth ->
            userService.getUserByUserId(auth.uid).get().addOnSuccessListener { user ->
                (user.get("followings") as? List<*>)
                    ?.mapNotNull { it as? String }
                    ?.map { postService.getAllPostsByUser(it) }
                    ?.forEach { query ->
                        query.get().addOnSuccessListener {
                            _posts.value = _posts.value?.plus(it.documents) ?: it.documents
                        }
                    }
            }
        }
    }
}
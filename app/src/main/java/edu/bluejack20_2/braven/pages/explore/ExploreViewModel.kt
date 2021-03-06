package edu.bluejack20_2.braven.pages.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.bluejack20_2.braven.domains.explore.ExploreService
import edu.bluejack20_2.braven.domains.user.UserService
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val exploreService: ExploreService,
    private val userService: UserService
) : ViewModel() {
    private val _originalPosts: MutableLiveData<List<DocumentSnapshot>> by lazy {
        MutableLiveData<List<DocumentSnapshot>>().also { refresh() }
    }

    private val _posts = MutableLiveData<List<DocumentSnapshot>>()

    val posts: LiveData<List<DocumentSnapshot>> = _posts

    val category = MutableLiveData("")
    val title = MutableLiveData("")
    val description = MutableLiveData("")
    val username = MutableLiveData("")
    val startDate = MutableLiveData(0L)
    val endDate = MutableLiveData(0L)

    fun refresh() {
        exploreService.getAllPosts().get().addOnSuccessListener {
            _originalPosts.value = it.documents
            _posts.value = it.documents
        }
    }

    fun applyFilter() {
        _posts.value = _originalPosts.value?.filter { doc ->
            val hasCategory = doc.getString("category")
                ?.contains(category.value.toString(), true) == true

            val hasTitle = doc.getString("title")
                ?.contains(title.value.toString(), true) == true

            val hasDescription = doc.getString("description")
                ?.contains(description.value.toString(), true) == true

            doc.getString("userId")?.let { id ->
                userService.getUserById(id).get().addOnSuccessListener { user ->
                    val hasUsername = user.getString("displayName")
                        ?.contains(username.value.toString(), true) == true

                    if (!hasUsername) {
                        _posts.value = _posts.value?.toMutableList()?.also {
                            it.remove(doc)
                        }
                    }
                }
            }

            val startDate = startDate.value ?: 0
            val endDate = endDate.value ?: 0

            // divided by 1000 to convert milliseconds to seconds

            val isAfter = doc.getTimestamp("timestamp")?.seconds ?: 0 >= (startDate / 1000)
            val isBefore = doc.getTimestamp("timestamp")?.seconds ?: 0 <= (endDate / 1000)

            var isInPeriod = true
            if (startDate > 0 && endDate > 0) {
                isInPeriod = isAfter && isBefore
            } else if (startDate > 0) {
                isInPeriod = isAfter
            } else if (endDate > 0) {
                isInPeriod = isBefore
            }

            return@filter hasCategory && hasTitle && hasDescription && isInPeriod
        } ?: emptyList()
    }

    fun resetFiler() {
        listOf(category, title, description, username).forEach { it.value = "" }
        listOf(startDate, endDate).forEach { it.value = 0L }
    }
}
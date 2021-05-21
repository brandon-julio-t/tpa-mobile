package edu.bluejack20_2.braven.domains.following

import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.braven.domains.user.UserRepository
import javax.inject.Inject

class FollowingUserService @Inject constructor(private val repository: UserRepository) {

    private val db = FirebaseFirestore.getInstance().collection("users")

    fun getAllUserFollowing(listOfString : List<String>) = listOfString.map {
        repository.getById(it)
    }

    fun getUserByUserId(id: String) = db.document(id)


}
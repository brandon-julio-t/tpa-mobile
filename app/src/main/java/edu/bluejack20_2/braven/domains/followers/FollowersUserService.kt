package edu.bluejack20_2.braven.domains.followers

import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.braven.domains.user.UserRepository
import javax.inject.Inject

class FollowersUserService @Inject constructor(private val repository: UserRepository) {

    private val db = FirebaseFirestore.getInstance().collection("users")

    fun getAllUserFollowers(followerIds: List<String>) = followerIds.map { id ->
        repository.getById(id)
    }

    fun getUserByUserId(id: String) = db.document(id)

}
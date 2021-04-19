package edu.bluejack20_2.braven.domains.following

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import javax.inject.Inject

class FollowingUserService @Inject constructor() {

    private val db = FirebaseFirestore.getInstance().collection("users")

    fun getAllUserFollowing(id: String) = db.document(id)

    fun getUserByUserId(id: String) = db.document(id)


}
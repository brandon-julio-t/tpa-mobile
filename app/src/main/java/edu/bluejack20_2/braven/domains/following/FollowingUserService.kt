package edu.bluejack20_2.braven.domains.following

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FollowingUserService @Inject constructor() {

    private val db = FirebaseFirestore.getInstance().collection("users")

    fun getAllUserFollowing(listOfString : List<String>) = db.whereIn("userId", listOfString)

    fun getUserByUserId(id: String) = db.document(id)


}
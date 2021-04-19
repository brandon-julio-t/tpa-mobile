package edu.bluejack20_2.braven.domains.followers

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FollowersUserService @Inject constructor(){

    private val db = FirebaseFirestore.getInstance().collection("users")

    fun getAllUserFollowers(id: String) = db.document(id)

    fun getUserByUserId(id: String) = db.document(id)

}
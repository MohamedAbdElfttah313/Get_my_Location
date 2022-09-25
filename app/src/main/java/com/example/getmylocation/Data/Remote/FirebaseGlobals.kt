package com.example.getmylocation.Data.Remote

import com.example.getmylocation.CloudMessagingService.MessagingService
import com.example.getmylocation.Domain.Models.NewUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.example.getmylocation.Data.Remote.FirebaseGlobals.Constants.*
import com.google.firebase.firestore.DocumentReference

class FirebaseGlobals {

    enum class Constants(val const : String){
        FRIENDS("Friends"),
        PENDING_FRIENDS("PendingFriends"),
        PENDED_REQUESTS("PendedRequests"),
    }

    init {
        getCurrentUserDocumentInfo()
    }


    fun updateFriends() {
        getAllFriends()
    }

    fun updatePendingFriends() {
        getAllPending()
    }

    fun updateFriendsRequest() {
        getAllFriendsRequests()
    }

    fun updateUserInfo(){
        getCurrentUserDocumentInfo()
    }

    companion object {
        var friendsId = emptyList<String>()
        var pendingFriendsId = emptyList<String>()
        var friendRequests = emptyList<String>()
        var userInfo: NewUser? = null

        val fireStoreCollection = FirebaseFirestore.getInstance().collection("Users")
        var auth = FirebaseAuth.getInstance()



        private fun getAllFriends() {
            update(FRIENDS.const)
        }

        private fun getAllPending() {
            update(PENDING_FRIENDS.const)
        }

        private fun getAllFriendsRequests() {
            update(PENDED_REQUESTS.const)
        }

        private fun update(collectionPath : String){
            auth.currentUser.let {
                fireStoreCollection.document(auth.currentUser!!.uid).collection(collectionPath)
                    .get()
                    .addOnSuccessListener {query->
                        when(collectionPath){
                            FRIENDS.const-> friendsId = query.map { it["Ref"] as String }
                            PENDING_FRIENDS.const -> pendingFriendsId = query.map { it["Ref"] as String }
                            PENDED_REQUESTS.const -> friendRequests = query.map { it["Ref"] as String }
                        }
                    }
            }
        }

        private fun getCurrentUserDocumentInfo() {
            fireStoreCollection.document(auth.currentUser!!.uid).run {
                get().addOnSuccessListener {
                    val dataWithoutToken = it.toObject(NewUser::class.java)
                    dataWithoutToken!!.token = MessagingService.token
                    set(dataWithoutToken , SetOptions.merge())
                    userInfo = dataWithoutToken
                }
            }

        }
    }
}
package com.example.getmylocation.Domain.Repositories

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot


interface FriendsRepository {
    fun getCurrentUserInfo() : Task<DocumentSnapshot>
    fun getFriends() : List<Task<DocumentSnapshot>>
    fun getFriendRequests() : List<Task<DocumentSnapshot>>
    fun sendOrUnsend(uid : String , send : Boolean) : Task<Void>
    fun getSearchResultBy(field : String) : Task<QuerySnapshot>
    fun updateFriendAccepted(uid : String) : MutableLiveData<Boolean>
    fun updateSelfAccFriend(uid : String):MutableLiveData<Boolean>
    fun updateFriendDeclined(uid :String) : MutableLiveData<Boolean>
    fun updateSelfDecFriend(uid :String) : MutableLiveData<Boolean>
}
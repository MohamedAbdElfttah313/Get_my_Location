package com.example.getmylocation.UI.CurrentUser.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.getmylocation.Data.Remote.FirebaseGlobals
import com.example.getmylocation.Domain.Models.NewUser
import com.example.getmylocation.Domain.UseCases.FriendsUseCases.*
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(): ViewModel() {

    init {
        FirebaseGlobals().apply{
            updateFriends()
            updateFriendsRequest()
        }
    }

    @Inject
    lateinit var getFriendsData : getFriendsUseCase

    @Inject
    lateinit var getFriendRequestsData : getFriendRequestsUseCase

    @Inject
    lateinit var updateFriendAcc : updateFriendAcceptedUseCase

    @Inject
    lateinit var updateSelfFriendAcc : updateSelfFriendAccUseCase

    @Inject
    lateinit var updateFriendDec : updateFriendDecUseCase

    @Inject
    lateinit var updateSelfFriendDec : updateSelfFriendDecUseCase

    val friends = MutableLiveData<List<NewUser>>()
    val friendRequests = MutableLiveData<List<NewUser>>()
    private val dummyFriends = ArrayList<NewUser>()
    private val dummyFriendRequests = ArrayList<NewUser>()

    fun getFriends(){
        val tasks = Tasks.whenAllSuccess<DocumentSnapshot>(getFriendsData())
        tasks.addOnSuccessListener {
            try {
                for (docSnapShot in it){
                    dummyFriends.add(docSnapShot.toObject(NewUser::class.java)!!)
                }
            }finally {
                friends.value = dummyFriends
            }

        }
    }

    fun getFriendRequests(){
        val tasks = Tasks.whenAllSuccess<DocumentSnapshot>(getFriendRequestsData())
        tasks.addOnSuccessListener {
            try {
                for (docSnapShot in it){
                    dummyFriendRequests.add(docSnapShot.toObject(NewUser::class.java)!!)
                }
            }finally {
                friendRequests.value = dummyFriendRequests
            }
        }
    }

    var acceptedFriendLiveDataRes = MutableLiveData<Boolean>()
    var acceptedSelfFriendLiveDataRes = MutableLiveData<Boolean>()
    var declinedFriendLiveDataRes = MutableLiveData<Boolean>()
    var declinedSelfFriendLiveDataRes = MutableLiveData<Boolean>()

    fun updateSelfAcceptFriend(acceptedFriendID : String){
        acceptedSelfFriendLiveDataRes = updateSelfFriendAcc(acceptedFriendID)
    }

    fun updateSelfDeclinedFriend(declinedFriendID : String){
        declinedSelfFriendLiveDataRes = updateSelfFriendDec(declinedFriendID)
    }

    fun updateAcceptedFriend(acceptedFriendID : String){
        acceptedFriendLiveDataRes = updateFriendAcc(acceptedFriendID)
    }

    fun updateDeclinedFriend(declinedFriendID : String){
        declinedFriendLiveDataRes = updateFriendDec(declinedFriendID)
    }
}
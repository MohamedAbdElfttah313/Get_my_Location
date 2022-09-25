package com.example.getmylocation.UI.CurrentUser.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.getmylocation.Data.Remote.FirebaseGlobals
import com.example.getmylocation.Domain.Models.NewUser
import com.example.getmylocation.Domain.UseCases.FriendsUseCases.getSearchResultByUseCase
import com.example.getmylocation.Domain.UseCases.FriendsUseCases.sendOrUnsendUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(): ViewModel() {

    init {
        FirebaseGlobals().updatePendingFriends()
    }

    @Inject
    lateinit var searchResUseCase : getSearchResultByUseCase

    @Inject
    lateinit var sendOrNot : sendOrUnsendUseCase

    val searchRes = MutableLiveData<List<NewUser>>()

    fun searchBy(name : String){
        searchResUseCase(name).addOnSuccessListener { res->
            searchRes.value = res.toObjects(NewUser::class.java).filter {
                it.uid != FirebaseGlobals.auth.currentUser!!.uid
            }
        }.addOnFailureListener {
            searchRes.value = emptyList()
        }
    }

    val sendSuccessfully = MutableLiveData<Boolean>()
    val unsendSuccessfully = MutableLiveData<Boolean>()

    fun sendOrUnsend(uid : String , send : Boolean){
        sendOrNot(uid , send).addOnSuccessListener {
            FirebaseGlobals().updatePendingFriends()
            if (send){
                sendSuccessfully.value = true
            }else{
                unsendSuccessfully.value = true
            }
        }
    }
}
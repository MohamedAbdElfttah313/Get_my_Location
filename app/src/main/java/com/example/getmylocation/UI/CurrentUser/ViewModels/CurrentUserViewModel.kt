package com.example.getmylocation.UI.CurrentUser.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.getmylocation.CloudMessagingService.MessagingService
import com.example.getmylocation.Data.Remote.FirebaseGlobals
import com.example.getmylocation.Domain.Models.NewUser
import com.example.getmylocation.Domain.UseCases.AuthUseCases.signOutUseCase
import com.example.getmylocation.Domain.UseCases.FriendsUseCases.getCurrentUserUseCase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CurrentUserViewModel @Inject constructor() : ViewModel() {

    val TYPE: String = "/topics/testTypeGET"
    init{
        FirebaseMessaging.getInstance().apply {
            subscribeToTopic(TYPE)
            token.addOnSuccessListener {
                MessagingService.token = it

                with(FirebaseGlobals()){
                    updateUserInfo()
                    updateFriendsRequest()
                }
            }
        }
    }

    @Inject
    lateinit var getCurrentUserUseCase: getCurrentUserUseCase

    @Inject
    lateinit var signOutNow : signOutUseCase

    val currentUserInfo = MutableLiveData<NewUser?>()

    fun getCurrentUser(){
        getCurrentUserUseCase().addOnSuccessListener {
            currentUserInfo.value = it.toObject(NewUser::class.java)
        }.addOnFailureListener {
            currentUserInfo.value = null
        }

    }

    fun signOut(){
        signOutNow()
    }


}
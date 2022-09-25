package com.example.getmylocation.Domain.UseCases.FriendsUseCases

import androidx.lifecycle.MutableLiveData

interface updateFriendAcceptedUseCase {
    operator fun invoke(uid : String):MutableLiveData<Boolean>
}
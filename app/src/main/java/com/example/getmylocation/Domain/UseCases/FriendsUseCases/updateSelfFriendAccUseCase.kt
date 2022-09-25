package com.example.getmylocation.Domain.UseCases.FriendsUseCases

import androidx.lifecycle.MutableLiveData

interface updateSelfFriendAccUseCase {
    operator fun invoke(uid : String) : MutableLiveData<Boolean>
}
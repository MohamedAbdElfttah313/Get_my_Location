package com.example.getmylocation.Domain.UseCases.FriendsUseCases

import androidx.lifecycle.MutableLiveData

interface updateSelfFriendDecUseCase {
    operator fun invoke(uid : String) : MutableLiveData<Boolean>
}
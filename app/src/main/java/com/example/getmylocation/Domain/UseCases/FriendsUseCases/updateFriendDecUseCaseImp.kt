package com.example.getmylocation.Domain.UseCases.FriendsUseCases

import androidx.lifecycle.MutableLiveData
import com.example.getmylocation.Domain.Repositories.FriendsRepository
import javax.inject.Inject

class updateFriendDecUseCaseImp @Inject constructor(private val repo : FriendsRepository) : updateFriendDecUseCase {
    override fun invoke(uid: String): MutableLiveData<Boolean> {
        return repo.updateFriendDeclined(uid)
    }
}
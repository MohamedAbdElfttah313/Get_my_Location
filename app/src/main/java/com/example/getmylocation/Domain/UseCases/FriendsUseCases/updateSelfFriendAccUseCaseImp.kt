package com.example.getmylocation.Domain.UseCases.FriendsUseCases

import androidx.lifecycle.MutableLiveData
import com.example.getmylocation.Domain.Repositories.FriendsRepository
import javax.inject.Inject

class updateSelfFriendAccUseCaseImp @Inject constructor(private val repo : FriendsRepository) : updateSelfFriendAccUseCase {
    override fun invoke(uid: String): MutableLiveData<Boolean> {
        return repo.updateSelfAccFriend(uid)
    }
}
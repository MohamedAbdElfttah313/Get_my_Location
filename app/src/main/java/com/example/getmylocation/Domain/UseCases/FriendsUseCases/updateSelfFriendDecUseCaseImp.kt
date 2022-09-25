package com.example.getmylocation.Domain.UseCases.FriendsUseCases

import androidx.lifecycle.MutableLiveData
import com.example.getmylocation.Domain.Repositories.FriendsRepository
import javax.inject.Inject

class updateSelfFriendDecUseCaseImp @Inject constructor(private val repo : FriendsRepository) : updateSelfFriendDecUseCase {
    override fun invoke(uid: String): MutableLiveData<Boolean> {
        return repo.updateSelfDecFriend(uid)
    }
}
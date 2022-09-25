package com.example.getmylocation.Domain.UseCases.FriendsUseCases

import com.example.getmylocation.Domain.Repositories.FriendsRepository
import com.google.android.gms.tasks.Task
import javax.inject.Inject

class sendOrUnsendUseCaseImp @Inject constructor(private val repo : FriendsRepository) : sendOrUnsendUseCase {
    override fun invoke(uid : String , send : Boolean): Task<Void> {
        return repo.sendOrUnsend(uid , send)
    }
}
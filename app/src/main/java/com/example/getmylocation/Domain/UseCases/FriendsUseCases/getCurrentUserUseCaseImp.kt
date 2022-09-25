package com.example.getmylocation.Domain.UseCases.FriendsUseCases

import com.example.getmylocation.Domain.Repositories.FriendsRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class getCurrentUserUseCaseImp @Inject constructor (private val repo : FriendsRepository) :
    getCurrentUserUseCase {
    override fun invoke(): Task<DocumentSnapshot> {
        return repo.getCurrentUserInfo()
    }
}
package com.example.getmylocation.Domain.UseCases.FriendsUseCases

import com.example.getmylocation.Domain.Repositories.FriendsRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class getFriendsUseCaseImp @Inject constructor(private val repo : FriendsRepository) : getFriendsUseCase {
    override fun invoke(): List<Task<DocumentSnapshot>> {
        return repo.getFriends()
    }
}
package com.example.getmylocation.Domain.UseCases.FriendsUseCases

import com.example.getmylocation.Domain.Repositories.FriendsRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class getFriendRequestsUseCaseImp @Inject constructor(private val repo : FriendsRepository) : getFriendRequestsUseCase {
    override fun invoke(): List<Task<DocumentSnapshot>> {
        return repo.getFriendRequests()
    }
}
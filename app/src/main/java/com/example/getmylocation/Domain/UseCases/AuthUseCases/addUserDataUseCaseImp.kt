package com.example.getmylocation.Domain.UseCases.AuthUseCases

import com.example.getmylocation.Domain.Models.NewUser
import com.example.getmylocation.Domain.Repositories.AuthRepository
import com.google.android.gms.tasks.Task
import javax.inject.Inject

class addUserDataUseCaseImp @Inject constructor(private val repo : AuthRepository) : addUserDataUseCase {
    override fun invoke(user: NewUser): Task<Void> {
       return repo.addUserData(user)
    }
}
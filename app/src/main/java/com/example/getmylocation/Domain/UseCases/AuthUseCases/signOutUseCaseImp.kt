package com.example.getmylocation.Domain.UseCases.AuthUseCases

import com.example.getmylocation.Domain.Repositories.AuthRepository
import javax.inject.Inject

class signOutUseCaseImp @Inject constructor(private val repo : AuthRepository) : signOutUseCase {
    override fun invoke() {
        repo.signOut()
    }
}
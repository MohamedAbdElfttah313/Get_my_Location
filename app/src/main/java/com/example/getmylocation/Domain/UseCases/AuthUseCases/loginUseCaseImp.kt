package com.example.getmylocation.Domain.UseCases.AuthUseCases

import com.example.getmylocation.Domain.Repositories.AuthRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class loginUseCaseImp @Inject constructor(private val repo : AuthRepository) : loginUseCase {
    override fun invoke(email: String, password: String): Task<AuthResult> {
        return repo.loginWithEmailAndPassword(email , password)
    }
}
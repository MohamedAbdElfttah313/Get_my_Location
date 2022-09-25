package com.example.getmylocation.Domain.UseCases.AuthUseCases

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

interface signUpUseCase {
    operator fun invoke(email : String , password : String) : Task<AuthResult>
}
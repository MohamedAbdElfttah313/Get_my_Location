package com.example.getmylocation.Domain.UseCases.AuthUseCases

import com.example.getmylocation.Domain.Models.NewUser
import com.google.android.gms.tasks.Task

interface addUserDataUseCase {
    operator fun invoke(user : NewUser) : Task<Void>
}
package com.example.getmylocation.Domain.UseCases.FriendsUseCases

import com.google.android.gms.tasks.Task

interface sendOrUnsendUseCase {
    operator fun invoke(uid : String , send : Boolean) : Task<Void>
}
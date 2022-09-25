package com.example.getmylocation.Domain.UseCases.FriendsUseCases

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot

interface getCurrentUserUseCase {
    operator fun invoke(): Task<DocumentSnapshot>
}
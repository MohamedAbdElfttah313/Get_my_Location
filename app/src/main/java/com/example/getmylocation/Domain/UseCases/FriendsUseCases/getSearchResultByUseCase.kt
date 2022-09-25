package com.example.getmylocation.Domain.UseCases.FriendsUseCases

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

interface getSearchResultByUseCase {
    operator fun invoke(field : String) : Task<QuerySnapshot>
}
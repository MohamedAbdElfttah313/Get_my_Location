package com.example.getmylocation.Domain.UseCases.FriendsUseCases

import com.example.getmylocation.Domain.Repositories.FriendsRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class getSearchResultByUseCaseImp @Inject constructor(private val repo : FriendsRepository): getSearchResultByUseCase {
    override fun invoke(field: String): Task<QuerySnapshot> {
        return repo.getSearchResultBy(field)
    }
}
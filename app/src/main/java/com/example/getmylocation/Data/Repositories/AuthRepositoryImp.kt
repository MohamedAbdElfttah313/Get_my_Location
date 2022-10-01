package com.example.getmylocation.Data.Repositories

import com.example.getmylocation.Data.Remote.FirebaseGlobals
import com.example.getmylocation.Domain.Models.NewUser
import com.example.getmylocation.Domain.Repositories.AuthRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.SetOptions
import javax.inject.Inject

class AuthRepositoryImp @Inject constructor() : AuthRepository {

    override fun signOut() {

        FirebaseGlobals.auth.signOut()

        FirebaseGlobals.fireStoreCollection.document(FirebaseGlobals.userInfo!!.uid!!).set(
            FirebaseGlobals.userInfo!!.apply {
                token= ""
            },
            SetOptions.merge()
        )

    }

    override fun loginWithEmailAndPassword(loginEmail: String, loginPassword: String): Task<AuthResult> {
        return FirebaseGlobals.auth.signInWithEmailAndPassword(loginEmail , loginPassword)
    }

    override fun signUp(signUpEmail: String, signUpPassword: String): Task<AuthResult> {
        return FirebaseGlobals.auth.createUserWithEmailAndPassword(signUpEmail , signUpPassword)
    }

    override fun addUserData(user: NewUser): Task<Void> {
        return FirebaseGlobals.fireStoreCollection.document(user.uid!!).set(user)
    }
}
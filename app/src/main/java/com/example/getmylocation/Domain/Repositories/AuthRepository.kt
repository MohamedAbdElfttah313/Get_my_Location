package com.example.getmylocation.Domain.Repositories

import com.example.getmylocation.Domain.Models.NewUser
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

interface AuthRepository {

    fun signOut() : Unit
    fun loginWithEmailAndPassword(loginEmail: String , loginPassword: String):Task<AuthResult>
    fun signUp(signUpEmail : String , signUpPassword : String) : Task<AuthResult>
    fun addUserData(user : NewUser) : Task<Void>
}
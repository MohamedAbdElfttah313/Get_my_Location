package com.example.getmylocation.UI.RegistrationAndLogin.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.getmylocation.Domain.Models.NewUser
import com.example.getmylocation.Domain.UseCases.AuthUseCases.addUserDataUseCase
import com.example.getmylocation.Domain.UseCases.AuthUseCases.signUpUseCase
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.StringBuilder
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var signUpUseCase: signUpUseCase

    @Inject
    lateinit var addUserData : addUserDataUseCase

    val signUpResult = MutableLiveData<Boolean>()
    var signUpResultMessage = ""

    fun signUp(email : String , password : String){
        signUpUseCase(email, password).addOnSuccessListener{
            signUpResultMessage = it.user!!.uid
            signUpResult.value = true

        }.addOnFailureListener {
            signUpResultMessage = it.message!!
            signUpResult.value = false
        }
    }

    fun addUserToFireStore(user : NewUser) : Task<Void>{
        return addUserData(user)
    }
}
package com.example.getmylocation.UI.RegistrationAndLogin.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.getmylocation.Domain.UseCases.AuthUseCases.loginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.StringBuilder
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var loginUseCase: loginUseCase

    val logInResult = MutableLiveData<Boolean>()
    var logInResultMessage = ""

    fun login(email : String , password : String){
        loginUseCase(email, password).addOnSuccessListener {
            logInResult.value = true
        }.addOnFailureListener {
            logInResultMessage = it.message!!
            logInResult.value = false
        }
    }

}
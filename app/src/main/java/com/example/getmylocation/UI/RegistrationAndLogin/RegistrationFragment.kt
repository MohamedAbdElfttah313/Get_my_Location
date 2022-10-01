package com.example.getmylocation.UI.RegistrationAndLogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.getmylocation.Domain.Models.NewUser
import com.example.getmylocation.R
import com.example.getmylocation.UI.Dialogs.PickADateFragmentDialogue
import com.example.getmylocation.UI.RegistrationAndLogin.ViewModels.RegistrationViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_regestiration.*


@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    lateinit var auth : FirebaseAuth
    lateinit var firestoreRef : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_regestiration,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestoreRef = FirebaseFirestore.getInstance()

        val dateOfBirth = StringBuilder()

        RegisterDateOfBirth.setOnClickListener{
            PickADateFragmentDialogue{
                dateOfBirth.append("${it[0]}/${it[1]}/${it[2]}")
                RegisterDateOfBirth.setText(dateOfBirth.toString())
            }.show(childFragmentManager , "anything")
        }

        val registerViewModel : RegistrationViewModel by viewModels()
        registerViewModel.signUpResult.observe(this){
            if (it){
                val firstName = RegisterFirstName.text.toString().trim()
                val lastName = RegisterLastName.text.toString().trim()

                val newUser = NewUser(firstName,lastName,
                    dateOfBirth.toString(),
                    registerViewModel.signUpResultMessage,
                    "$firstName $lastName".toLowerCase(),
                    "","","")


                registerViewModel.addUserToFireStore(newUser).addOnSuccessListener {
                    parentFragmentManager.popBackStack()
                    Toast.makeText(requireContext() , "Registered Successfully" , Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(requireContext() , registerViewModel.signUpResultMessage , Toast.LENGTH_SHORT).show()
                Register.isEnabled = true
            }
        }

        Register.setOnClickListener {
            Register.isEnabled=false
            RegisterErrorNotes.text = ""

            if (noEmptyFields() && passwordsConforms() && passwordRegex()){
                val email = RegisterEmail.text.toString().trim()
                val password = RegisterPassword.text.toString()
                registerViewModel.signUp(email,password)
            }else{
                RegisterErrorNotes.append(getErrorNotes())
                Register.isEnabled=true
            }

        }
    }

    private fun getErrorNotes():String{
        val errorNotes = StringBuilder().run {
            append((if (noEmptyFields().not())"\n-Some fields are not filled" else ""))
            append((if (passwordsConforms().not())"\n-Both Passwords are not equal!" else ""))
            append((if (passwordRegex().not())"\n-Password MUST contain at least 8 characters, 1 uppercase letter, 1 lowercase letter and 1 number" else ""))
            toString()
        }
        return errorNotes
    }

    private fun noEmptyFields() : Boolean{
        return (RegisterEmail.text.toString().isNotBlank() &&
                RegisterFirstName.text.toString().isNotBlank() &&
                RegisterLastName.text.toString().isNotBlank() &&
                RegisterDateOfBirth.text.toString().isNotBlank() &&
                RegisterPassword.text.toString().isNotBlank() &&
                ConfirmRegisterPassword.text.toString().isNotBlank())
    }

    private fun passwordsConforms():Boolean{
        return ((RegisterPassword.text.toString()) == (ConfirmRegisterPassword.text.toString()))
    }

    private fun passwordRegex() : Boolean{

        return (Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$").run { RegisterPassword.text.toString().matches(this) })
    }


}
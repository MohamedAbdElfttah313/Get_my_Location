package com.example.getmylocation.UI.RegistrationAndLogin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.getmylocation.UI.CurrentUser.Activities.CurrentUserActivity
import com.example.getmylocation.Data.Remote.FirebaseGlobals
import com.example.getmylocation.R
import com.example.getmylocation.UI.RegistrationAndLogin.ViewModels.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class LoginFragment : Fragment() {

    lateinit var auth: FirebaseAuth
    

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login , container , false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        requestProvidersPermission()

        val loginViewModel : LoginViewModel by viewModels()
        loginViewModel.logInResult.observe(this){
            if (it){
                Toast.makeText(requireContext(), "Logged in Successfully", Toast.LENGTH_SHORT).show()
                toCurrentUserActivity()
            }else{
                Toast.makeText(requireContext(),loginViewModel.logInResultMessage, Toast.LENGTH_SHORT).show()
                LogInButton.isEnabled = true
            }
        }

        LogInButton.setOnClickListener {
            val email = EmailSignInEditText.text.toString().trim()
            val password = PasswordSignInEditText.text.toString()

            if (email.isNotBlank() && password.isNotBlank()){
                LogInButton.isEnabled = false
                loginViewModel.login(email,password)
            }
        }



        RegisterButton.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.RegistrationLoginFragmentContainer , RegistrationFragment())
                .addToBackStack("RegFragment").commit()
        }
    }

    override fun onStart() {
        super.onStart()
        auth.currentUser?.let {
            toCurrentUserActivity()
        }
    }

    private fun toCurrentUserActivity() {

        requireActivity().startActivity(Intent(requireContext(), CurrentUserActivity::class.java)
            .apply { setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK) })
    }

    private fun requestProvidersPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),
                1233
            )
        }
    }
}


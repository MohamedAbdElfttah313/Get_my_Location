package com.example.getmylocation.UI.RegistrationAndLogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.getmylocation.CloudMessagingService.MessagingService
import com.example.getmylocation.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_login)

        MessagingService.preferences = getSharedPreferences("TokensPref" , MODE_PRIVATE)
        supportFragmentManager.beginTransaction().add(R.id.RegistrationLoginFragmentContainer , LoginFragment()).commit()

    }
}
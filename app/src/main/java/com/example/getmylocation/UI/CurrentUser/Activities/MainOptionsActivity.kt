package com.example.getmylocation.UI.CurrentUser.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.getmylocation.UI.CurrentUser.Fragments.FriendsFragment
import com.example.getmylocation.UI.CurrentUser.Fragments.ProfileFragment
import com.example.getmylocation.UI.CurrentUser.Fragments.SearchFragment
import com.example.getmylocation.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainOptionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_options)

        val selectedFragment = when(intent.getStringExtra("SelectedMainFragment")){
            "Profile"-> ProfileFragment()
            "Friends"-> FriendsFragment()
            "Search"->  SearchFragment()
            else -> ProfileFragment()
        }

        supportFragmentManager.beginTransaction().replace(R.id.OptionsFragmentContainer , selectedFragment).commit()
    }
}
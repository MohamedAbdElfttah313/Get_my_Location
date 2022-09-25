package com.example.getmylocation.UI.CurrentUser.Activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.getmylocation.Data.Remote.FirebaseGlobals
import com.example.getmylocation.UI.CurrentUser.*
import com.example.getmylocation.UI.CurrentUser.CurrentUserViewPager.ViewPagerAdapter
import com.example.getmylocation.UI.Dialogs.PickProfilePictureDialog
import com.example.getmylocation.R
import com.example.getmylocation.UI.RegistrationAndLogin.RegistrationLoginActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.example.getmylocation.Data.Remote.FirebaseGlobals.Constants.*
import com.example.getmylocation.UI.CurrentUser.ViewModels.CurrentUserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_current_user.*

@AndroidEntryPoint
class CurrentUserActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_current_user)

        UserViewPager2.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(tabLayoutCurrentUser, UserViewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Tracker"
                1 -> tab.text = "Help"
            }
        }.attach()

        val currentUserViewModel : CurrentUserViewModel by viewModels()
        currentUserViewModel.currentUserInfo.observe(this){
            it?.let {
                if (it.profilePicture.isNullOrEmpty()) {
                    PickProfilePictureDialog { pic ->
                        Glide.with(this@CurrentUserActivity).load(Uri.parse(pic)).into(PersonalImage)
                        FirebaseGlobals().updateUserInfo()
                    }.show(supportFragmentManager, "pickPic")

                } else {
                    Glide.with(this@CurrentUserActivity).load(Uri.parse(it.profilePicture))
                        .into(PersonalImage)
                }
            }
        }

        currentUserViewModel.getCurrentUser()

        profileUserButton.setOnClickListener {
            startFragment("Profile")
        }

        searchUsersButton.setOnClickListener {
            startFragment("Search")
        }

        FriendsImageButton.setOnClickListener {
            startFragment("Friends")
        }

        LogOutButton.setOnClickListener {
            startActivity(Intent(applicationContext, RegistrationLoginActivity::class.java).apply {
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
            currentUserViewModel.signOut()

        }

        FirebaseGlobals.fireStoreCollection.document(FirebaseGlobals.auth.currentUser!!.uid).collection(FRIENDS.const)
            .addSnapshotListener { value, error ->
                if (error == null && value!!.isEmpty.not() && value!=null) {
                    FirebaseGlobals().updateFriends()
                }
            }

    }

    private fun startFragment(fragmentName : String){
        startActivity(Intent(this, MainOptionsActivity::class.java)
            .apply {
                putExtra("SelectedMainFragment",fragmentName)
            })
    }






}
package com.example.getmylocation.UI.CurrentUser.CurrentUserViewPager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.getmylocation.CloudMessagingService.NewUserPushNotification
import com.example.getmylocation.CloudMessagingService.sendNotificationOb
import com.example.getmylocation.Data.Remote.FirebaseGlobals
import com.example.getmylocation.UI.Dialogs.NotificationSenderDialog
import com.example.getmylocation.R
import com.example.getmylocation.Domain.Models.NewUser
import kotlinx.android.synthetic.main.fragment_current_location.*
import kotlinx.android.synthetic.main.notifications_sender_dialog.*
import kotlinx.coroutines.*

class CurrentLocationFragment() : Fragment() {


    lateinit var coroScope : CoroutineScope

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_location, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        coroScope = CoroutineScope(Job() + Dispatchers.IO)

        HelpButton.setOnClickListener {

                FirebaseGlobals.userInfo.let {
                    NotificationSenderDialog{ toWhom->
                        coroScope.launch {
                            sendNotification(it , toWhom)
                        }
                    }.apply {
                        show(this@CurrentLocationFragment.childFragmentManager , "NotificationSenderDialog")
                    }
                }
        }
    }


    private suspend fun sendNotification(user : NewUser?, to : Int){
        val notificationHelpSender = sendNotificationOb.sendNotification
        when(to){
            0->{
                notificationHelpSender.pushNotification(
                    NewUserPushNotification(user!! ,"/topics/testTypeGET")
                )
            }

            1->{
                for (i in FirebaseGlobals.friendsId){
                    FirebaseGlobals.fireStoreCollection.document(i).get()
                        .addOnSuccessListener {
                           coroScope.launch {
                               notificationHelpSender.pushNotification(
                                   NewUserPushNotification(user!!.apply { topic="HELP_FRIEND" } , it["token"] as String)
                               )
                           }
                        }

                }
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        coroScope.cancel()
    }
}
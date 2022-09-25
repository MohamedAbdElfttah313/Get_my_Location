package com.example.getmylocation

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {


    override fun onCreate() {
        super.onCreate()

        val ChannelId = "Channel1"

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(ChannelId , "Cloud Messaging",NotificationManager.IMPORTANCE_HIGH).apply {
                description = "CLoud messaging service"
            }
            val notManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notManager.createNotificationChannel(notificationChannel)
        }



    }

}
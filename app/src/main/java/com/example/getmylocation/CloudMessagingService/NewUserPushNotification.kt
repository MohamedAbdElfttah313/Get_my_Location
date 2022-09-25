package com.example.getmylocation.CloudMessagingService

import com.example.getmylocation.Domain.Models.NewUser

data class NewUserPushNotification(
    val data: NewUser,
    val to : String
)
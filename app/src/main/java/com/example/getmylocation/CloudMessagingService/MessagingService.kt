package com.example.getmylocation.CloudMessagingService

import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.example.getmylocation.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class MessagingService : FirebaseMessagingService() {

    companion object {

        var preferences : SharedPreferences? = null
        var token : String?
        get() {
            return (preferences!!.getString("Token","")!!)
        }
        set(value) {
            preferences!!.edit().putString("Token",value).apply()
        }

        val DUMMY_PICTURE = "https://firebasestorage.googleapis.com/v0/b/get-my-location-5b14c.appspot.com/o/Dummy%2FdummyProfilePicture.png?alt=media&token=03d8261e-1033-4b6e-851b-e286746e07fb"

    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        token = newToken
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        println("Sent")
        if (message.data.containsKey("topic")){
            when(message.data["topic"]){
                "FRIEND_REQUEST"-> sentFriendRequestNotification(message)
                "FRIEND_ACCEPTED"-> sentFriendAcceptedNotification(message)
                "HELP_FRIEND"-> sentFriendToHelp(message)
            }
        }


        when(message.from.toString()){
            "/topics/testTypeGET"->{
                helpNotification(message)
            }
        }

    }

    private fun helpNotification(remoteMessage : RemoteMessage){

        val pic =remoteMessage.data["profilePicture"].let {
            if (it!!.isBlank()) DUMMY_PICTURE else it
        }

        val senderPicture = Glide.with(applicationContext).asBitmap()
            .load(Uri.parse(pic))
            .submit().get()

        sendHelpNotification("SOMEONE NEEDS HELP!",
            "HELP IS NEEDED IN THE ATTACHED PLACE!!",
            remoteMessage.data["lat"]!!.toDouble() , remoteMessage.data["lon"]!!.toDouble(),
            senderPicture)

    }

    private fun sentFriendRequestNotification(remoteMessage : RemoteMessage){
        val pic =remoteMessage.data["profilePicture"].let {
            if (it!!.isBlank()) DUMMY_PICTURE else it
        }

        val senderPicture = Glide.with(applicationContext).asBitmap()
            .load(Uri.parse(pic))
            .submit().get()

        sendNotification("Friend Request",
            "${remoteMessage.data["firstName"]} ${remoteMessage.data["lastName"]} has sent you a friend request",
            4040,
            senderPicture)
    }

    private fun sentFriendAcceptedNotification(remoteMessage : RemoteMessage){
        val pic =remoteMessage.data["profilePicture"].let {
            if (it!!.isBlank()) DUMMY_PICTURE else it
        }

        val senderPicture = Glide.with(applicationContext).asBitmap()
            .load(Uri.parse(pic))
            .submit().get()

        sendNotification("Friend Update",
            "${remoteMessage.data["firstName"]} ${remoteMessage.data["lastName"]} Has Accepted Your Friend Request",
            Random.nextInt(1100,1200),
            senderPicture)
    }

    private fun sentFriendToHelp(remoteMessage: RemoteMessage) {
        val senderPicture = Glide.with(applicationContext).asBitmap()
            .load(Uri.parse(remoteMessage.data["profilePicture"]))
            .submit().get()

        sendHelpNotification("FRIEND NEEDS HELP!",
            "${remoteMessage.data["nameForSearch"]!!.toUpperCase()} NEEDS HELP RIGHT NOW!!",
            remoteMessage.data["lat"]!!.toDouble() , remoteMessage.data["lon"]!!.toDouble(),
            senderPicture)
    }

    private fun sendNotification(Title : String , Text : String , id : Int, largeIcon : Bitmap = BitmapFactory.decodeResource(resources , R.drawable.circlemenuhere)){

        val notification = NotificationCompat.Builder(this ,"Channel1")
            .setContentTitle(Title)
            .setContentText(Text)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(largeIcon)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        with(NotificationManagerCompat.from(this)){
            notify(id , notification)
        }
    }

    private fun sendHelpNotification(Title : String , Text: String , lat : Double , lon : Double ,largeIcon : Bitmap = BitmapFactory.decodeResource(resources , R.drawable.circlemenuhere)){

        val mapUri = Uri.parse("google.navigation:q=$lat,$lon")
        val locationIntent = Intent(Intent.ACTION_VIEW , mapUri).apply {
            setPackage("com.google.android.apps.maps")
        }
        val notification = NotificationCompat.Builder(this ,"Channel1")
            .setContentTitle(Title)
            .setContentText(Text)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(largeIcon)
            .setContentIntent(PendingIntent.getActivity(this , 12 , locationIntent,0))
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        with(NotificationManagerCompat.from(this)){
            notify(9999 , notification)
        }
    }
}
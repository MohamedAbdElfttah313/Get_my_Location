package com.example.getmylocation.UI.CurrentUser.CurrentUserViewPager

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.getmylocation.CloudMessagingService.NewUserPushNotification
import com.example.getmylocation.CloudMessagingService.sendNotificationOb
import com.example.getmylocation.Data.Remote.FirebaseGlobals
import com.example.getmylocation.UI.Dialogs.NotificationSenderDialog
import com.example.getmylocation.R
import com.example.getmylocation.Domain.Models.NewUser
import com.example.getmylocation.UtilAndSystemServices.ProvidersStates
import kotlinx.android.synthetic.main.fragment_current_location.*
import kotlinx.android.synthetic.main.fragment_tracker.*
import kotlinx.android.synthetic.main.notifications_sender_dialog.*
import kotlinx.coroutines.*

class CurrentLocationFragment() : Fragment() , LocationListener {


    var latCur : Double = 0.0
    var lonCur : Double = 0.0
    val REFRESH_TIMER : Long = 5000
    val REFRESH_DISTANCE : Float = 1f
    lateinit var locationManager: LocationManager

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

        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        HelpButton.setOnClickListener {

            val canListen = ProvidersStates().run {
                isAnyNetworkStateEnabled(requireContext()) &&
                        isLocationProvidersEnabled(
                            LocationManager.GPS_PROVIDER,
                            LocationManager.NETWORK_PROVIDER,requireContext())
            }

            if (canListen){
                activateLocationListener()

                FirebaseGlobals.userInfo.let {
                    NotificationSenderDialog{ toWhom->
                        coroScope.launch {
                            sendNotification(it , toWhom)
                        }
                    }.apply {
                        show(this@CurrentLocationFragment.childFragmentManager , "NotificationSenderDialog")
                    }
                }
            }else{
                Toast.makeText(requireContext() , "GPS Disabled or Bad Connection" , Toast.LENGTH_SHORT).show()
            }

        }
    }


    private suspend fun sendNotification(user : NewUser?, to : Int){
        val notificationHelpSender = sendNotificationOb.sendNotification
        when(to){
            0->{
                notificationHelpSender.pushNotification(
                    NewUserPushNotification(user!!.apply {
                        lat = latCur
                        lon = lonCur
                    }
                        ,"/topics/testTypeGET")
                )
            }

            1->{
                for (i in FirebaseGlobals.friendsId){
                    FirebaseGlobals.fireStoreCollection.document(i).get()
                        .addOnSuccessListener {
                           coroScope.launch {
                               notificationHelpSender.pushNotification(
                                   NewUserPushNotification(user!!.apply {
                                       topic="HELP_FRIEND"
                                       lat = latCur
                                       lon = lonCur
                                       }
                                       , it["token"] as String)
                               )
                           }
                        }

                }
            }
        }


    }

    @SuppressLint("MissingPermission")
    private fun activateLocationListener(){
        with(locationManager){
            requestLocationUpdates(android.location.LocationManager.GPS_PROVIDER , REFRESH_TIMER , REFRESH_DISTANCE , this@CurrentLocationFragment)
            requestLocationUpdates(android.location.LocationManager.NETWORK_PROVIDER , REFRESH_TIMER , REFRESH_DISTANCE , this@CurrentLocationFragment)
        }
    }

    override fun onLocationChanged(p0: Location) {
        latCur = p0.latitude
        lonCur = p0.longitude
        locationManager.removeUpdates(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroScope.cancel()
    }
}
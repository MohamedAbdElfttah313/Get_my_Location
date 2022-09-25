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
import com.example.getmylocation.R
import com.example.getmylocation.UtilAndSystemServices.ProvidersStates
import com.example.getmylocation.UtilAndSystemServices.TimeFormatter
import kotlinx.android.synthetic.main.fragment_tracker.*


class TrackerFragment : Fragment() , LocationListener {

    var lat : Double = 0.0
    var lon : Double = 0.0
    val REFRESH_TIMER : Long = 5000
    val REFRESH_DISTANCE : Float = 1f
    lateinit var locationManager : LocationManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_tracker, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager



        StartTrackButton.setOnClickListener {
            val canListen = ProvidersStates().run {
                isAnyNetworkStateEnabled(requireContext()) &&
                        isLocationProvidersEnabled(LocationManager.GPS_PROVIDER,LocationManager.NETWORK_PROVIDER,requireContext())
            }

            if (canListen){
                activateLocationListener()
                StartTrackButton.isEnabled = false
            }else{
                Toast.makeText(requireContext() , "GPS Disabled or Bad Connection" , Toast.LENGTH_SHORT).show()
            }
        }



    }

    @SuppressLint("MissingPermission")
    private fun activateLocationListener(){
        with(locationManager){
            requestLocationUpdates(LocationManager.GPS_PROVIDER , REFRESH_TIMER , REFRESH_DISTANCE , this@TrackerFragment)
            requestLocationUpdates(LocationManager.NETWORK_PROVIDER , REFRESH_TIMER , REFRESH_DISTANCE , this@TrackerFragment)
        }
    }

    override fun onLocationChanged(p0: Location) {

        lat = p0.latitude
        lon = p0.longitude

        TrackerTextView.append("[${TimeFormatter().getTimeFormatted("hh:mm:ss aa")}] ${p0.provider}\nLatitude:$lat , Longitude:$lon\n\n")

    }

    override fun onProviderEnabled(provider: String) {
        println("Enabled")
        super.onProviderEnabled(provider)
    }


    override fun onProviderDisabled(provider: String) {
        super.onProviderDisabled(provider)

        println("Remove")
        locationManager.removeUpdates(this)

        StartTrackButton.isEnabled = true

    }
}
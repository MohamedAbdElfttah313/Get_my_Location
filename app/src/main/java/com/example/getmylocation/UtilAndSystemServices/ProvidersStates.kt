package com.example.getmylocation.UtilAndSystemServices

import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class ProvidersStates {
    fun isLocationProvidersEnabled(provider1 : String , provider2 : String ,context : Context):Boolean{
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return (lm.isProviderEnabled(provider1) &&lm.isProviderEnabled(provider2))
    }

    fun isAnyNetworkStateEnabled(context : Context):Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){

           val network = connectivityManager.activeNetwork?: return false
           val activeNetwork = connectivityManager.getNetworkCapabilities(network)?: return false
           val arrayOfCapabilities = arrayOf(NetworkCapabilities.NET_CAPABILITY_INTERNET , NetworkCapabilities.NET_CAPABILITY_VALIDATED,
               NetworkCapabilities.TRANSPORT_WIFI , NetworkCapabilities.TRANSPORT_CELLULAR).map {
                   activeNetwork.hasCapability(it)
           }.filter{
               it
           }
           return arrayOfCapabilities.isNotEmpty()

       }else{
           return connectivityManager.activeNetworkInfo?.isConnected?:false
       }
    }
}
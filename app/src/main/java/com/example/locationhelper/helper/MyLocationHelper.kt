package com.example.locationhelper.helper

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.locationhelper.helper.Constants.TAG
import com.example.wasalni.common.uitils.MyLocationHelperListener
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import javax.inject.Inject


class MyLocationHelper @Inject constructor() {
    private var mGpsSwitchStateReceiver: BroadcastReceiver?=null
     private var mLocationRequest: LocationRequest? =null
    private var mLocationCallback: LocationCallback?=null
     fun getLocation(context:Context,locationHelperListener: MyLocationHelperListener){
        val lm = context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
//                showGpsAlert()
            Log.i(Constants.TAG, "setLocation:LocationManager.GPS_PROVIDER ")
            locationHelperListener.cannotAccessLocation()
        }
             mLocationRequest = LocationRequest.create()
            mLocationRequest!!.interval = 60000
            mLocationRequest!!.fastestInterval = 5000
            mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

             mLocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location = locationResult.lastLocation
                    locationHelperListener.lastLocation(location)
                    locationResult.locations.forEach {
                        locationHelperListener.updatesLocation(it)
                    }
                    Log.i("GAMALRAGAB", "onLocationResult: ${location}")
                }
            }

            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            LocationServices.getFusedLocationProviderClient(context).requestLocationUpdates(mLocationRequest!!, mLocationCallback!!, Looper.myLooper()!!);

        setBordCastLocationListener(context,locationHelperListener)
    }

     private fun setBordCastLocationListener(
         mcontext: Context,
         locationHelperListener: MyLocationHelperListener
     ) {
          mGpsSwitchStateReceiver = object : BroadcastReceiver() {
             override fun onReceive(context: Context?, intent: Intent) {
                 try {
                     val locationManager =mcontext.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
                     val isGpsEnabled =
                         locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                     if (isGpsEnabled) {
                        //locationHelperListener.cannotAccessLocation()
                     } else {
                         locationHelperListener.cannotAccessLocation()
                     }
                 } catch (ex: Exception) {

                 }

             }
         }

         mcontext.registerReceiver(mGpsSwitchStateReceiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));

     }

     fun onStopMyLocation(context: Context){
         Log.i(TAG,"onStopMyLocation: ")
         mGpsSwitchStateReceiver?.let {
             context.unregisterReceiver(it)
         }
         mLocationCallback?.let {
             LocationServices.getFusedLocationProviderClient(context).removeLocationUpdates(it)
         }
     }

 }
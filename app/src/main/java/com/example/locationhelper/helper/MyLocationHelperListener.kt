package com.example.wasalni.common.uitils

import android.location.Location

interface MyLocationHelperListener {

    fun lastLocation(location:Location)
    fun updatesLocation(location:Location)
    fun cannotAccessLocation()
}
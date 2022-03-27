package com.example.locationhelper.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.locationhelper.helper.Constants.TAG
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SharedViewModel @Inject constructor(
) : ViewModel() {

    private val _userLocationState = MutableLiveData<LatLng>()
    val userLocationState:LiveData<LatLng> = _userLocationState




    fun setUserLocation(latLng: LatLng) {
            Log.i(TAG, "setUserLocation: ${latLng.toString()}")
            _userLocationState.postValue(latLng)
        }



}
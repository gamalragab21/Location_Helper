package com.example.locationhelper

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.animation.Animation
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.locationhelper.databinding.ActivityMainBinding
import com.example.locationhelper.helper.*
import com.example.locationhelper.helper.Constants.TAG
import com.example.locationhelper.viewmodel.SharedViewModel
import com.example.wasalni.common.uitils.MyLocationHelperListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.makeramen.roundedimageview.RoundedDrawable
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() ,EasyPermissions.PermissionCallbacks,
    MyLocationHelperListener {
    @Inject lateinit var myLocationHelper:MyLocationHelper
    private lateinit var binding: ActivityMainBinding
    private var map: GoogleMap? = null
    private var lastLatLng: LatLng = LatLng(0.0, 0.0)
    private var lastMarker: Marker? = null
    private val sharedViewModel:SharedViewModel by viewModels()
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        subscribeToUserLocation()

        Log.i(TAG, ": OnMapReadyCallback")
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */


        // setupStyleForMap(googleMap)


    }

    private fun subscribeToUserLocation() {
        sharedViewModel.userLocationState.observe(this){
            setupMarkerRestaurantAndUserLocation( it)
        }
    }

    private fun setupMarkerRestaurantAndUserLocation(currentUserLocation: LatLng) {
        val mapView = binding.mapView

        if (lastLatLng.latitude > 0 && lastLatLng.longitude > 0) {
            return
        }
        mapView.clearAnimation()
        lastMarker?.remove()
        lastLatLng = currentUserLocation
        val drawable: Drawable? =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_arround_location, null)

        val bitmap = RoundedDrawable.drawableToBitmap(drawable!!)
        map?.let { googleMap ->
            val groundOverlayRestaurant = googleMap.addGroundOverlay(
                GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromBitmap(bitmap!!))
                    .position(currentUserLocation, 200f)
            )

            val groundAnimationRestaurant = RadiusAnimation(groundOverlayRestaurant!!).apply {
                repeatCount = Animation.INFINITE
                repeatMode = Animation.RESTART
                duration = 2000
            }
            mapView.startAnimation(groundAnimationRestaurant) // MapView where i show my map


            //  addCircle(googleMap,dataUserInfo.latLng)

            val option = MarkerOptions().position(currentUserLocation)
                .icon(bitmapDescriptorFromVector(this, R.drawable.ic_arround_location))
            lastMarker = googleMap.addMarker(option)


//            googleMap.addMarker(MarkerOptions().position(currentUserLocation))
            //  addCircle(googleMap,restaurantLocation)
            val restaurantCameraPosition = buildCameraPosition(currentUserLocation)

//        addPolyLine(googleMap,userLocation,restaurantLocation)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentUserLocation, 17f))
        }
    }
    private fun bitmapDescriptorFromVector(
        context: Context,
        @DrawableRes vectorResId: Int,
    ): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(1,
            1,
            Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


    private fun buildCameraPosition(latLng: LatLng) =
        CameraPosition.Builder()
            .target(latLng) // Sets the center of the map to Mountain View
            .zoom(13f) // Sets the zoom
            .bearing(90f) // Sets the orientation of the camera to east
            .tilt(30f) // Sets the tilt of the camera to 30 degrees
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myLocationHelper= MyLocationHelper()
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(callback)
    }
    private fun requestLocationPermissions() {

        when {
            PermissionsUtility.hasLocationPermissions(this) -> {
                myLocationHelper.getLocation(this, this)
                return
            }
            else -> when {
                Build.VERSION.SDK_INT < Build.VERSION_CODES.Q -> {
                    EasyPermissions.requestPermissions(
                        this,
                        getString(R.string.read_permmission_location_message_permissions),
                        Constants.REQUEST_CODE_LOCATION_PERMISSIONS,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                }
                else -> {
                    EasyPermissions.requestPermissions(
                        this,
                        getString(R.string.read_permmission_location_message_permissions),
                        Constants.REQUEST_CODE_LOCATION_PERMISSIONS,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                }
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_CODE_LOCATION_PERMISSIONS)
            EasyPermissions.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults,
                this
            )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        myLocationHelper.getLocation(this, this)

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestLocationPermissions()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        myLocationHelper.onStopMyLocation(this)

    }

    override fun lastLocation(location: Location) {
        Log.i(TAG, "lastLocation: ${location}")
        CustomDialog.dialog?.dismiss()

        sharedViewModel.setUserLocation(LatLng(location.latitude,location.longitude))

    }

    override fun updatesLocation(location: Location) {
        Log.i(TAG, "updatesLocation: $location")
    }

    override fun cannotAccessLocation() {
        Log.i(TAG, "cannotAccessLocation: ")
        Toast.makeText(this, "Must open location to can complete", Toast.LENGTH_SHORT).show()
        CustomDialog.dialog?.dismiss()
        CustomDialog.showDialogToEnableLocation(this) {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }
    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
        Log.i(Constants.TAG, ": onResume")

    }

    override fun onStart() {
        super.onStart()
        Log.i(Constants.TAG, ": home onStart")
             requestLocationPermissions()
         binding.mapView.onStart()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        Log.i(Constants.TAG, ":home onStop")
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        Log.i(Constants.TAG, ":home onPause")

        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }


}
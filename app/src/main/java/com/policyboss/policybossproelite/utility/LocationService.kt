package com.policyboss.policybossproelite.utility

import android.Manifest
import android.app.Activity
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*


class LocationService(context: Activity, locationListner: ILocation?) {

    var mContext: Activity
    var locationListner: ILocation? = null

    lateinit var locationRequest: LocationRequest
    //var  locationRequest: LocationRequest
    //  private LocationCallback locationCallback ;

    //  private LocationCallback locationCallback ;
    //  private LocationCallback locationCallback ;
    val MY_PERMISSION_REQUEST_FINE_LOCATION: Int = 101
    val REQUEST_CHECK_SETTINGS = 0x1

    var fusedLocationProviderClient: FusedLocationProviderClient? = null

    init {

        mContext = context
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext!!)

        this.locationListner = locationListner

//        locationRequest = LocationRequest.create()
//        locationRequest.setInterval(10000)
//        locationRequest.setFastestInterval(5000)
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

    }


    var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            if(locationListner != null){

                locationListner?.getLocation(locationResult.lastLocation) // calling Interface method
            }

        }
    }


//    fun setLocationListener(locationListner: ILocation?) {
//        this.locationListner = locationListner
//    }


    fun createLocationRequest() {

        locationRequest = LocationRequest.create()
        locationRequest.setInterval(10000)
        locationRequest.setFastestInterval(5000)
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)


        builder.setAlwaysShow(true)

        val client = LocationServices.getSettingsClient(mContext)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener(mContext!!) {
           // Toast.makeText(mContext, "addOnSuccessListener", Toast.LENGTH_SHORT).show()
            startLocationUpdates()
        }

        task.addOnFailureListener(mContext!!) { e ->
           // Toast.makeText(mContext, "No Location Found, Please try Again!!", Toast.LENGTH_SHORT).show()
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(
                        mContext!!,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    //Not in used
    private fun turnOnGPS() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result =
            LocationServices.getSettingsClient(mContext)
                .checkLocationSettings(builder.build())
        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(
                    ApiException::class.java
                )
                Toast.makeText(mContext, "GPS is already tured on", Toast.LENGTH_SHORT)
                    .show()
            } catch (e: ApiException) {
                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvableApiException = e as ResolvableApiException
                        resolvableApiException.startResolutionForResult(mContext, 2)
                    } catch (ex: SendIntentException) {
                        ex.printStackTrace()
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                }
            }
        }
    }


    fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                mContext!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient!!.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mContext!!.requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSION_REQUEST_FINE_LOCATION
                )
            }
        }
    }

    fun stopLocationUpdates() {
        if (locationCallback != null) {
            fusedLocationProviderClient!!.removeLocationUpdates(locationCallback)
        }
    }




    interface ILocation {
        fun getLocation(location: Location?)
    }

}
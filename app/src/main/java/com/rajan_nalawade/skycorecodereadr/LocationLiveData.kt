package com.rajan_nalawade.skycorecodereadr


import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.rajan_nalawade.skycorecodereadr.models.LocationModel

class LocationLiveData(context: Context) : LiveData<LocationModel>() {

    private val mContext = context
    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    override fun onInactive() {
        super.onInactive()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.also {
                    setLocationData(mContext, it)
                }
            }
        //startLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()!!
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult ?: return
            for (location in locationResult.locations) {
                setLocationData(mContext, location)
            }

        }
    }

    private fun setLocationData(context: Context, location: Location) {

        val city = getCity(context, location)

        value = LocationModel(
            longitude = location.longitude,
            latitude = location.latitude,
            city = city
        )
    }

    private fun getCity(mCotext: Context, location: Location): String {
        val geocoder = Geocoder(mCotext)
        val list = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        return list.get(0).subAdminArea
    }

    companion object {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
}
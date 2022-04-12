package com.rajan_nalawade.skycorecodereadr.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.rajan_nalawade.skycorecodereadr.LocationLiveData

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationData = LocationLiveData(application)

    fun getLocationData() = locationData
}
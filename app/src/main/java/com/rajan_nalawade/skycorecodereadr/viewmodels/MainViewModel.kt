package com.rajan_nalawade.skycorecodereadr.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rajan_nalawade.skycorecodereadr.models.Businesse
import com.rajan_nalawade.skycorecodereadr.repository.YelpRepository

class MainViewModel(private val mYelpRepository: YelpRepository) : ViewModel() {

    private var _radious = MutableLiveData<Int>(100)
    val radious: LiveData<Int> = _radious

    fun getRestaurantsByRadius(city: String, mRadious: Int): LiveData<PagingData<Businesse>> {
        return mYelpRepository.getRestaurants(city, mRadious).cachedIn(viewModelScope)
    }

    fun updateRadious(radios: Int) {
        _radious.value = radios
    }
}

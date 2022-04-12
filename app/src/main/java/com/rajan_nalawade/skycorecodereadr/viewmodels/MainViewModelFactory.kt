package com.rajan_nalawade.skycorecodereadr.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rajan_nalawade.skycorecodereadr.repository.YelpRepository

class MainViewModelFactory(private val mYelpRepository: YelpRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(mYelpRepository) as T
    }

}
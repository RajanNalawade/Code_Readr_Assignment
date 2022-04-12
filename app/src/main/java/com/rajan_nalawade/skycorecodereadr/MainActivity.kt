package com.rajan_nalawade.skycorecodereadr

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajan_nalawade.skycorecodereadr.api.RetrofitHelper
import com.rajan_nalawade.skycorecodereadr.api.YelpApi
import com.rajan_nalawade.skycorecodereadr.databinding.ActivityMainBinding
import com.rajan_nalawade.skycorecodereadr.paging.RestaurantsPagingAdapter
import com.rajan_nalawade.skycorecodereadr.repository.YelpRepository
import com.rajan_nalawade.skycorecodereadr.utils.Constants.GPS_REQUEST
import com.rajan_nalawade.skycorecodereadr.utils.Constants.LOCATION_PERMISSION_REQ_CODE
import com.rajan_nalawade.skycorecodereadr.utils.Constants.arrPermissionRuntime
import com.rajan_nalawade.skycorecodereadr.utils.GPSUtils
import com.rajan_nalawade.skycorecodereadr.viewmodels.LocationViewModel
import com.rajan_nalawade.skycorecodereadr.viewmodels.MainViewModel
import com.rajan_nalawade.skycorecodereadr.viewmodels.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mRestaurantPagingAdapter: RestaurantsPagingAdapter

    private lateinit var locationViewModel: LocationViewModel
    private var isGPSEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val mYelpApi = RetrofitHelper.getInstance().create(YelpApi::class.java)
        val mRepository = YelpRepository(mYelpApi)

        mMainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(mRepository)
        ).get(MainViewModel::class.java)

        mRestaurantPagingAdapter = RestaurantsPagingAdapter()
        binding.rvRestaurants.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mRestaurantPagingAdapter
        }

        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        GPSUtils(this).turnGPSOn(object : GPSUtils.OnGpsListener {

            override fun gpsStatus(isGPSEnable: Boolean) {
                this@MainActivity.isGPSEnabled = isGPSEnable
            }
        })

        binding.swipeToRefresh.setOnRefreshListener {
            binding.progressBar.visibility = View.VISIBLE
            binding.rvRestaurants.visibility = View.GONE
            mRestaurantPagingAdapter.refresh()
            binding.swipeToRefresh.isRefreshing = false
        }

        binding.seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.textView2.text =
                    if (progress <= 1000) "${progress} meters" else "${progress / 1000.0} KM"
                Log.d("onProgressChanged", "${progress}")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Log.d("onStartTrackingTouch", "${seekBar?.progress}")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.progress?.let {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.rvRestaurants.visibility = View.GONE
                    //mMainViewModel.setRadious(seekBar.progress)
                    /*mMainViewModel.getRestaurantsByRadius(100).observe(, Observer {
                        binding.progressBar.visibility = View.GONE
                        binding.rvRestaurants.visibility = View.VISIBLE
                        mRestaurantPagingAdapter.submitData(lifecycle, it)
                    })*/
                    mMainViewModel.updateRadious(seekBar.progress)
                    //mRestaurantPagingAdapter.refresh()
                    //mMainViewModel.getRestaurantsByRadius(it)
                }
            }
        }
        )
    }

    override fun onStart() {
        super.onStart()
        invokeLocationAction()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
                isGPSEnabled = true
                invokeLocationAction()
            }
        }
    }

    private fun invokeLocationAction() {
        binding.progressBar.visibility = View.GONE
        binding.rvRestaurants.visibility = View.GONE

        when {
            !isGPSEnabled -> Toast.makeText(
                this,
                getString(R.string.enable_gps),
                Toast.LENGTH_SHORT
            ).show()

            isPermissionsGranted() -> startLocationUpdate()

            shouldShowRequestPermissionRationale() -> Toast.makeText(
                this,
                getString(R.string.permission_request),
                Toast.LENGTH_SHORT
            ).show()

            else -> ActivityCompat.requestPermissions(
                this,
                arrPermissionRuntime,
                LOCATION_PERMISSION_REQ_CODE
            )
        }
    }

    private fun startLocationUpdate() {
        locationViewModel.getLocationData().observe(this, Observer { location ->

            binding.progressBar.visibility = View.VISIBLE
            binding.rvRestaurants.visibility = View.GONE

            mMainViewModel.radious.observe(this, Observer {
                mMainViewModel.getRestaurantsByRadius("NYC", it).observe(this, Observer {
                    binding.progressBar.visibility = View.GONE
                    binding.rvRestaurants.visibility = View.VISIBLE
                    mRestaurantPagingAdapter.submitData(lifecycle, it)
                })
            })
        })
    }

    private fun isPermissionsGranted() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    private fun shouldShowRequestPermissionRationale() =
        ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) && ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQ_CODE -> {
                invokeLocationAction()
            }
        }
    }
}
package com.daon.goj_gam.screen.main.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.daon.goj_gam.R
import com.daon.goj_gam.data.entity.LocationLatLngEntity
import com.daon.goj_gam.data.entity.MapSearchInfoEntity
import com.daon.goj_gam.databinding.FragmentHomeBinding
import com.daon.goj_gam.screen.base.BaseFragment
import com.daon.goj_gam.screen.main.home.restaurant.RestaurantCategory
import com.daon.goj_gam.screen.main.home.restaurant.RestaurantListFragment
import com.daon.goj_gam.screen.mylocation.MyLocationActivity
import com.daon.goj_gam.widget.adapter.RestaurantListFragmentPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {


    override val viewModel by viewModel<HomeViewModel>()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    private lateinit var viewPagerAdapter: RestaurantListFragmentPagerAdapter

    private lateinit var locationManager: LocationManager

    private lateinit var myLocationListener: MyLocationListener

    private val changeLocationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getParcelableExtra<MapSearchInfoEntity>(HomeViewModel.MY_LOCATION_KEY)
                    ?.let { myLocationInfo ->
                        viewModel.loadReverseGeoInformation(myLocationInfo.locationLatLng)
                    }
            }
        }

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->

            val responsePermissions = permission.entries.filter {
                (it.key == Manifest.permission.ACCESS_FINE_LOCATION)
                        || (it.key == Manifest.permission.ACCESS_COARSE_LOCATION)
            }
            if (responsePermissions.filter { it.value == true }.size == locationPermissions.size) {
                setMyLocationListener()
            } else {
                with(binding.locationTitleText) {
                    setText(R.string.request_location_manager)
                    setOnClickListener {
                        getMyLocation()
                    }
                }
                Toast.makeText(
                    requireContext(),
                    getString(R.string.toast_not_assigned_location_permission),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun observeData() = viewModel.homeStateLiveData.observe(viewLifecycleOwner) {
        when (it) {
            HomeState.Uninitialized -> {
                getMyLocation()
            }
            HomeState.Loading -> {
                binding.locationLoading.isVisible = true
                binding.locationTitleText.text = getString(R.string.loading)
            }
            is HomeState.Success -> {
                binding.locationLoading.isGone = true
                binding.locationTitleText.text = it.mapSearchInfo.fullAddress
                binding.tabLayout.isVisible = true
                binding.filterScrollView.isVisible = true
                binding.viewPager.isVisible = true
                initViewPager(it.mapSearchInfo.locationLatLng)
            }
            is HomeState.Error -> {
                binding.locationLoading.isGone = true
                binding.locationTitleText.text = getString(R.string.error_location_dis_found)
                binding.locationTitleText.setOnClickListener {
                    getMyLocation()
                }
                Toast.makeText(requireContext(), it.messageId, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun initViews() = with(binding) {
        locationTitleText.setOnClickListener {
            viewModel.getMapSearchInfo()?.let { mapInfo ->
                changeLocationLauncher.launch(
                    MyLocationActivity.newIntent(
                        requireContext(), mapInfo
                    )
                )
            }
        }
    }

    private fun initViewPager(locationLatLng: LocationLatLngEntity) = with(binding) {
        val restaurantCategories = RestaurantCategory.values()

        if (::viewPagerAdapter.isInitialized.not()) {
            val restaurantListFragment = restaurantCategories.map {
                RestaurantListFragment.newInstance(it)
            }
            viewPagerAdapter = RestaurantListFragmentPagerAdapter(
                this@HomeFragment,
                restaurantListFragment
            )
            viewPager.adapter = viewPagerAdapter
        }
        viewPager.offscreenPageLimit = restaurantCategories.size
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setText(restaurantCategories[position].categoryNameId)
        }.attach()
    }

    private fun getMyLocation() {
        if (::locationManager.isInitialized.not()) {
            locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        val isGpsUnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGpsUnabled) {
            locationPermissionLauncher.launch(locationPermissions)
        }
    }

    @SuppressLint("MissingPermission")
    private fun setMyLocationListener() {
        val minTime = 1500L
        val minDistance = 100f
        if (::myLocationListener.isInitialized.not()) {
            myLocationListener = MyLocationListener()
        }
        with(locationManager) {
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime, minDistance, myLocationListener
            )
            requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime, minDistance, myLocationListener
            )
        }
    }

    companion object {

        val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        fun newInstance() = HomeFragment()

        const val TAG = "HomeFragment"

    }

    private fun removeLocationListener() {
        if (::locationManager.isInitialized && ::myLocationListener.isInitialized) {
            locationManager.removeUpdates(myLocationListener)
        }
    }

    inner class MyLocationListener : LocationListener {
        @SuppressLint("SetTextI18n")
        override fun onLocationChanged(location: Location) {
            // binding.locationTitleText.text = "${location.latitude}, ${location.longitude}"
            viewModel.loadReverseGeoInformation(
                LocationLatLngEntity(
                    location.latitude,
                    location.longitude
                )
            )
            removeLocationListener()
        }
    }

}
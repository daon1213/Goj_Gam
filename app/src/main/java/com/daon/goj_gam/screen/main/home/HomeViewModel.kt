package com.daon.goj_gam.screen.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.daon.goj_gam.R
import com.daon.goj_gam.data.entity.LocationLatLngEntity
import com.daon.goj_gam.data.entity.MapSearchInfoEntity
import com.daon.goj_gam.data.repository.map.MapRepository
import com.daon.goj_gam.data.repository.user.UserRepository
import com.daon.goj_gam.screen.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val mapRepository: MapRepository,
    private val userRepository: UserRepository
):BaseViewModel() {

    companion object {
        const val MY_LOCATION_KEY = "MyLocation"
    }

    val homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized)

    fun loadReverseGeoInformation(
        locationLatLngEntity: LocationLatLngEntity
    ) = viewModelScope.launch {
        homeStateLiveData.value = HomeState.Loading
        val userLocation = userRepository.getUserLocation()
        val currentLocation = userLocation ?: locationLatLngEntity

        val addressInfo = mapRepository.getReverseGeoInformation(currentLocation)
        addressInfo?.let { info ->
            homeStateLiveData.value = HomeState.Success(
                mapSearchInfo =  info.toSearchInfoEntity(locationLatLngEntity),
                isLocationSame = currentLocation == locationLatLngEntity
            )
        } ?: kotlin.run {
            homeStateLiveData.value = HomeState.Error(
                R.string.fail_to_load_address_info
            )
        }
    }

    fun getMapSearchInfo(): MapSearchInfoEntity? {
        when (val data = homeStateLiveData.value) {
            is HomeState.Success -> {
                return data.mapSearchInfo
            }
        }
        return null
    }

}
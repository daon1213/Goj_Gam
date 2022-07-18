package com.daon.goj_gam.screen.main.home.restaurant

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.daon.goj_gam.data.entity.impl.LocationLatLngEntity
import com.daon.goj_gam.data.repository.restaurant.RestaurantRepository
import com.daon.goj_gam.model.restaurant.RestaurantModel
import com.daon.goj_gam.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantListViewModel(
    private val restaurantCategory: RestaurantCategory,
    private var locationLatLng: LocationLatLngEntity,
    private val restaurantRepository: RestaurantRepository,
    private var restaurantOrder: RestaurantOrder = RestaurantOrder.DEFAULT
): BaseViewModel() {

    val restaurantListLiveData = MutableLiveData<List<RestaurantModel>>()

    override fun fetchData(): Job = viewModelScope.launch{
        val restaurantList = restaurantRepository.getList(restaurantCategory, locationLatLng)
        val sortedList = when (restaurantOrder) {
            RestaurantOrder.DEFAULT -> {
                restaurantList
            }
            RestaurantOrder.LOW_DELIVERY_TIP -> {
                restaurantList.sortedBy { it.deliveryTipRange.first }
            }
            RestaurantOrder.FAST_DELIVERY -> {
                restaurantList.sortedBy { it.deliveryTimeRange.first }
            }
            RestaurantOrder.TOP_RATE -> {
                restaurantList.sortedByDescending { it.grade }
            }
        }
        restaurantListLiveData.value = restaurantList.map {
            RestaurantModel(
                id = it.id,
                restaurantInfoId = it.restaurantInfoId,
                restaurantCategory = it.restaurantCategory,
                restaurantTitle = it.restaurantTitle,
                restaurantImageUrl = it.restaurantImageUrl,
                grade = it.grade,
                reviewCount = it.reviewCount,
                deliveryTimeRange = it.deliveryTimeRange,
                deliveryTipRange = it.deliveryTipRange,
                restaurantTelNumber = it.restaurantTelNumber
            )
        }
    }

    fun setLocationLatLng(locationLatLng: LocationLatLngEntity) {
        this.locationLatLng = locationLatLng
        fetchData()
    }

    fun setRestaurantOrder(order: RestaurantOrder) {
        this.restaurantOrder = order
    }

}
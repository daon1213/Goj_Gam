package com.daon.goj_gam.data.repository.restaurant

import com.daon.goj_gam.data.entity.impl.LocationLatLngEntity
import com.daon.goj_gam.data.entity.impl.RestaurantEntity
import com.daon.goj_gam.screen.main.home.restaurant.RestaurantCategory

interface RestaurantRepository {

    suspend fun getList(
        restaurantCategory: RestaurantCategory,
        locationLatLngEntity: LocationLatLngEntity
    ): List<RestaurantEntity>

}
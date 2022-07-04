package com.daon.goj_gam.data.repository.user

import com.daon.goj_gam.data.entity.impl.LocationLatLngEntity
import com.daon.goj_gam.data.entity.impl.RestaurantEntity

interface UserRepository {

    // location
    suspend fun getUserLocation(): LocationLatLngEntity?
    suspend fun insertUserLocation(locationLatLngEntity: LocationLatLngEntity)

    // liked restaurant
    // 실무에서는 반드시 id 로!
    suspend fun getUserLikedRestaurant(restaurantTitle: String): RestaurantEntity?
    suspend fun insertUserLikedRestaurant(restaurantEntity: RestaurantEntity)
    suspend fun deleteUserLikedRestaurant(restaurantTitle: String)
    suspend fun deleteAllUserLikedRestaurant()

    suspend fun getAllUserLikedRestaurant(): List<RestaurantEntity>

}
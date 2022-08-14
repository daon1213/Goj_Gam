package com.daon.goj_gam.data.entity.impl

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderEntity(
    val id: String,
    val userId: String,
    val restaurantId: Long,
    val foodMenuList: List<RestaurantFoodEntity>,
    val restaurantTitle: String
): Parcelable
package com.daon.goj_gam.data.entity

import android.os.Parcelable
import com.daon.goj_gam.screen.main.home.restaurant.RestaurantCategory
import kotlinx.android.parcel.Parcelize

@Suppress("DEPRECATED_ANNOTATION")
@Parcelize
data class RestaurantEntity(
    override val id: Long,
    val restaurantCategory: RestaurantCategory,
    val restaurantTitle: String,
    val restaurantImageUrl: String,
    val grade: Float,
    val reviewCount: Int,
    val deliveryTimeRange: Pair<Int, Int>,
    val deliveryTipRange: Pair<Int, Int>,
    val restaurantInfold: Int
): Entity, Parcelable
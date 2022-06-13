package com.daon.goj_gam.data.entity

import android.os.Parcelable
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.daon.goj_gam.screen.main.home.restaurant.RestaurantCategory
import com.daon.goj_gam.util.converter.RoomTypeConverters
import kotlinx.android.parcel.Parcelize

@Suppress("DEPRECATED_ANNOTATION")
@Parcelize
@androidx.room.Entity
@TypeConverters(RoomTypeConverters::class)
data class RestaurantEntity(
    override val id: Long,
    val restaurantCategory: RestaurantCategory,
    @PrimaryKey val restaurantTitle: String,
    val restaurantImageUrl: String,
    val grade: Float,
    val reviewCount: Int,
    val deliveryTimeRange: Pair<Int, Int>,
    val deliveryTipRange: Pair<Int, Int>,
    val restaurantInfoId: Long,
    val restaurantTelNumber: String?
) : Entity, Parcelable
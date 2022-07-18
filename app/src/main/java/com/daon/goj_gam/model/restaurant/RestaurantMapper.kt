package com.daon.goj_gam.model.restaurant

import com.daon.goj_gam.data.entity.impl.RestaurantEntity
import com.daon.goj_gam.model.CellType


fun RestaurantEntity.toModel(type: CellType): RestaurantModel {
    return RestaurantModel(
        id = id,
        type = type,
        restaurantInfoId = restaurantInfoId,
        restaurantCategory = restaurantCategory,
        restaurantTitle = restaurantTitle,
        restaurantImageUrl = restaurantImageUrl,
        grade = grade,
        reviewCount = reviewCount,
        deliveryTimeRange = deliveryTimeRange,
        deliveryTipRange = deliveryTipRange,
        restaurantTelNumber = restaurantTelNumber
    )
}

fun RestaurantModel.toEntity(): RestaurantEntity {
    return RestaurantEntity(
        id,
        restaurantCategory,
        restaurantTitle,
        restaurantImageUrl,
        grade,
        reviewCount,
        deliveryTimeRange,
        deliveryTipRange,
        restaurantInfoId,
        restaurantTelNumber
    )
}
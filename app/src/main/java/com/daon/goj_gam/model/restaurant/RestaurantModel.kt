package com.daon.goj_gam.model.restaurant

import com.daon.goj_gam.data.entity.RestaurantEntity
import com.daon.goj_gam.model.CellType
import com.daon.goj_gam.model.Model
import com.daon.goj_gam.screen.main.home.restaurant.RestaurantCategory


data class RestaurantModel(
    override val id: Long,
    override val type: CellType = CellType.RESTAURANT_CELL,
    val restaurantCategory: RestaurantCategory,
    val restaurantTitle: String,
    val restaurantImageUrl: String,
    val grade: Float,
    val reviewCount: Int,
    val deliveryTimeRange: Pair<Int, Int>,
    val deliveryTipRange: Pair<Int, Int>,
    val restaurantInfoId: Long,
) : Model(id, type) {

    fun toEntity() = RestaurantEntity(
        id,
        restaurantCategory,
        restaurantTitle,
        restaurantImageUrl,
        grade,
        reviewCount,
        deliveryTimeRange,
        deliveryTipRange,
        restaurantInfoId
    )

}
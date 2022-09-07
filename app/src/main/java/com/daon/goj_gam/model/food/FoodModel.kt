package com.daon.goj_gam.model.food

import com.daon.goj_gam.data.entity.impl.RestaurantFoodEntity
import com.daon.goj_gam.model.CellType
import com.daon.goj_gam.model.Model

data class FoodModel(
    override val id: Long, // for model
    override val type: CellType = CellType.FOOD_CELL,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val restaurantId: Long,
    val foodId: String,
    val restaurantTitle: String
): Model(id, type) {

    fun toEntity(basketIndex: Int) = RestaurantFoodEntity(
        "${foodId}_${basketIndex}",
        title,
        description,
        price,
        imageUrl,
        restaurantId,
        restaurantTitle
    )

}
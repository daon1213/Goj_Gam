package com.daon.goj_gam.screen.order

import androidx.annotation.StringRes
import com.daon.goj_gam.model.food.FoodModel

sealed interface OrderMenuState{

    object UnInitialized: OrderMenuState

    object Loading: OrderMenuState

    data class Success(
        val restaurantFoodModelList: List<FoodModel>? = null
    ): OrderMenuState

    object Order: OrderMenuState

    data class Error(
        @StringRes val messageId: Int,
        val e : Throwable
    ): OrderMenuState

}
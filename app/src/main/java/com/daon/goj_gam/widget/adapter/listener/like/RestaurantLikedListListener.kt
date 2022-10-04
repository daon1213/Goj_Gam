package com.daon.goj_gam.widget.adapter.listener.like

import com.daon.goj_gam.model.restaurant.RestaurantModel
import com.daon.goj_gam.widget.adapter.listener.restaurant.RestaurantListListener

interface RestaurantLikedListListener: RestaurantListListener {

    fun onDislikeItem(model: RestaurantModel)

}
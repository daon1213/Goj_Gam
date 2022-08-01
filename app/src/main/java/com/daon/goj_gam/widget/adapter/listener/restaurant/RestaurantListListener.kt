package com.daon.goj_gam.widget.adapter.listener.restaurant

import com.daon.goj_gam.model.restaurant.RestaurantModel
import com.daon.goj_gam.widget.adapter.listener.AdapterListener

interface RestaurantListListener: AdapterListener {

    fun onClickItem(model: RestaurantModel)

}
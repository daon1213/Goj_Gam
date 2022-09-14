package com.daon.goj_gam.widget.adapter.listener.food

import com.daon.goj_gam.model.food.FoodModel
import com.daon.goj_gam.widget.adapter.listener.AdapterListener

interface FoodMenuListListener: AdapterListener {
    fun onClickItem(model: FoodModel)
}
package com.daon.goj_gam.widget.adapter.listener.order

import com.daon.goj_gam.model.food.FoodModel
import com.daon.goj_gam.widget.adapter.listener.AdapterListener

/**
 * 장바구니 메뉴 클릭 리스너
 */
interface OrderMenuListListener: AdapterListener {
    fun onRemoveItem(foodModel: FoodModel)
}
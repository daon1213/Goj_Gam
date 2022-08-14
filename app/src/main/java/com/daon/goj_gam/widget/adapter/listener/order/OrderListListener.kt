package com.daon.goj_gam.widget.adapter.listener.order

import com.daon.goj_gam.model.order.OrderModel
import com.daon.goj_gam.widget.adapter.listener.AdapterListener

/**
 * 주문내역 아이템 클릭 리스너
 */
interface OrderListListener: AdapterListener {
    fun onClickItem(orderModel: OrderModel)
}
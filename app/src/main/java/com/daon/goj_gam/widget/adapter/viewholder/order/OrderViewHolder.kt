package com.daon.goj_gam.widget.adapter.viewholder.order

import com.daon.goj_gam.R
import com.daon.goj_gam.databinding.ViewholderOrderBinding
import com.daon.goj_gam.model.order.OrderModel
import com.daon.goj_gam.screen.base.BaseViewModel
import com.daon.goj_gam.util.provider.ResourcesProvider
import com.daon.goj_gam.widget.adapter.listener.order.OrderListListener
import com.daon.goj_gam.widget.adapter.viewholder.ModelViewHolder

class OrderViewHolder(
    private val binding: ViewholderOrderBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<OrderModel>(binding, viewModel, resourcesProvider) {
    override fun reset() {
    }

    override fun bindData(model: OrderModel) {
        super.bindData(model)
        with(binding) {
            orderTitleText.text =
                resourcesProvider.getString(R.string.order_history_title, model.orderId)
            val foodMenuList = model.foodMenuList

            var orderDataStr = ""

            foodMenuList
                .groupBy { it.title }
                .entries.forEach { (title, menuList) ->
                        orderDataStr += "메뉴 : $title | 가격 : ${menuList.first().price}원 X ${menuList.size}\n"
                    orderContentText.text = orderDataStr
                }
            orderContentText.text = orderContentText.text.trim()

            orderTotalPriceText.text =
                resourcesProvider.getString(
                    R.string.price,
                    foodMenuList.map { it.price }.reduce { total, price -> total + price }
                )
        }
    }

    override fun bindViews(model: OrderModel, adapterListener: Any) {
        if (adapterListener is OrderListListener) {
            binding.root.setOnClickListener {
                adapterListener.onClickItem(model)
            }
        }
    }
}
package com.haman.aop_part6_chapter01.widget.adapter.viewholder.impl.order

import com.daon.goj_gam.R
import com.daon.goj_gam.databinding.ViewholderOrderMenuBinding
import com.daon.goj_gam.extension.clear
import com.daon.goj_gam.extension.load
import com.daon.goj_gam.model.food.FoodModel
import com.daon.goj_gam.screen.base.BaseViewModel
import com.daon.goj_gam.util.provider.ResourcesProvider
import com.daon.goj_gam.widget.adapter.listener.order.OrderMenuListListener
import com.daon.goj_gam.widget.adapter.viewholder.ModelViewHolder

class OrderMenuViewHolder(
    private val binding: ViewholderOrderMenuBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
): ModelViewHolder<FoodModel>(binding, viewModel, resourcesProvider) {
    override fun reset() {
        binding.foodImage.clear()
    }

    override fun bindData(model: FoodModel) {
        super.bindData(model)
        with(binding) {
            foodImage.load(model.imageUrl, 24f)
            foodTitleText.text = model.title
            foodDescriptionText.text = model.description
            priceText.text = resourcesProvider.getString(R.string.price, model.price)
        }
    }

    override fun bindViews(model: FoodModel, adapterListener: Any) {
        if (adapterListener is OrderMenuListListener) {
            binding.removeButton.setOnClickListener {
                adapterListener.onRemoveItem(model)
            }
        }
    }
}
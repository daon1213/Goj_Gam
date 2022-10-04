package com.daon.goj_gam.widget.adapter.viewholder.like

import com.daon.goj_gam.R
import com.daon.goj_gam.databinding.ViewholderRestaurantLikedBinding
import com.daon.goj_gam.extension.clear
import com.daon.goj_gam.extension.load
import com.daon.goj_gam.model.restaurant.RestaurantModel
import com.daon.goj_gam.screen.base.BaseViewModel
import com.daon.goj_gam.util.provider.ResourcesProvider
import com.daon.goj_gam.widget.adapter.listener.like.RestaurantLikedListListener
import com.daon.goj_gam.widget.adapter.viewholder.ModelViewHolder

class RestaurantLikedListViewHolder(
    private val binding: ViewholderRestaurantLikedBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
): ModelViewHolder<RestaurantModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        restaurantImage.clear()
    }

    override fun bindData(model: RestaurantModel) = with(binding) {
        super.bindData(model)
        restaurantImage.load(model.restaurantImageUrl, 24f)
        restaurantTitleText.text = model.restaurantTitle
        gradeText.text = resourcesProvider.getString(R.string.grade_format, model.grade)
        reviewCountText.text = resourcesProvider.getString(R.string.review_count, model.reviewCount)

        val (minTime, maxTime) = model.deliveryTimeRange
        deliveryTimeText.text = resourcesProvider.getString(R.string.delivery_time, minTime, maxTime)

        val (minTip, maxTip) = model.deliveryTipRange
        deliveryTipText.text = resourcesProvider.getString(R.string.delivery_tip, minTip, maxTip)
    }

    override fun bindViews(model: RestaurantModel, adapterListener: Any) {
        if (adapterListener is RestaurantLikedListListener) {
            binding.root.setOnClickListener {
                adapterListener.onClickItem(model)
            }
            binding.likeImageButton.setOnClickListener {
                adapterListener.onDislikeItem(model)
            }
        }
    }
}
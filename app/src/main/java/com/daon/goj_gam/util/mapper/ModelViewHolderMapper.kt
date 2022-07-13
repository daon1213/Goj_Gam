package com.daon.goj_gam.util.mapper

import android.view.LayoutInflater
import android.view.ViewGroup
import com.daon.goj_gam.databinding.*
import com.daon.goj_gam.model.CellType
import com.daon.goj_gam.model.Model
import com.daon.goj_gam.screen.base.BaseViewModel
import com.daon.goj_gam.util.provider.ResourcesProvider
import com.daon.goj_gam.widget.adapter.viewholder.EmptyViewHolder
import com.daon.goj_gam.widget.adapter.viewholder.ModelViewHolder
import com.daon.goj_gam.widget.adapter.viewholder.food.FoodMenuViewHolder
import com.daon.goj_gam.widget.adapter.viewholder.like.RestaurantLikedListViewHolder
import com.daon.goj_gam.widget.adapter.viewholder.order.OrderViewHolder
import com.daon.goj_gam.widget.adapter.viewholder.restaurant.RestaurantViewHolder
import com.daon.goj_gam.widget.adapter.viewholder.review.ReviewViewHolder
import com.haman.aop_part6_chapter01.widget.adapter.viewholder.impl.order.OrderMenuViewHolder

object ModelViewHolderMapper {

    @Suppress("UNCHECKED_CAST")
    fun <M : Model> map(
        parent: ViewGroup,
        type: CellType,
        viewModel: BaseViewModel,
        resourcesProvider: ResourcesProvider
    ): ModelViewHolder<M> {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = when (type) {
            CellType.EMPTY_CELL -> EmptyViewHolder(
                ViewholderEmptyBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.RESTAURANT_CELL -> RestaurantViewHolder(
                ViewholderRestaurantBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.FOOD_CELL -> FoodMenuViewHolder(
                ViewholderFoodMenuBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.REVIEW_CELL -> ReviewViewHolder(
                ViewholderReviewBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.LIKED_RESTAURANT_CELL ->  RestaurantLikedListViewHolder(
                ViewholderRestaurantLikedBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.ORDER_FOOD_CELL -> OrderMenuViewHolder(
                ViewholderOrderMenuBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.ORDER_CELL -> OrderViewHolder(
                ViewholderOrderBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
        }
        return viewHolder as ModelViewHolder<M>
    }

}
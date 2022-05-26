package com.daon.goj_gam.util.mapper

import android.view.LayoutInflater
import android.view.ViewGroup
import com.daon.goj_gam.databinding.ViewholderEmptyBinding
import com.daon.goj_gam.databinding.ViewholderRestaurantBinding
import com.daon.goj_gam.model.CellType
import com.daon.goj_gam.model.CellType.EMPTY_CELL
import com.daon.goj_gam.model.CellType.RESTAURANT_CELL
import com.daon.goj_gam.model.Model
import com.daon.goj_gam.screen.base.BaseViewModel
import com.daon.goj_gam.util.provider.ResourcesProvider
import com.daon.goj_gam.widget.adapter.viewholder.EmptyViewHolder
import com.daon.goj_gam.widget.adapter.viewholder.ModelViewHolder
import com.daon.goj_gam.widget.adapter.viewholder.restaurant.RestaurantViewHolder

object ModelViewHolderMapper {

    @Suppress("UNCHECKED_CAST")
    fun <M: Model> map(
        parent: ViewGroup,
        type: CellType,
        viewModel: BaseViewModel,
        resourcesProvider: ResourcesProvider
    ): ModelViewHolder<M> {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = when(type) {
            EMPTY_CELL -> EmptyViewHolder(
                ViewholderEmptyBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            RESTAURANT_CELL -> RestaurantViewHolder(
                ViewholderRestaurantBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
        }
        return viewHolder as ModelViewHolder<M>
    }
}
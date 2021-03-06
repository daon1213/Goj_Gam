package com.daon.goj_gam.widget.adapter.viewholder

import com.daon.goj_gam.databinding.ViewholderEmptyBinding
import com.daon.goj_gam.model.Model
import com.daon.goj_gam.screen.base.BaseViewModel
import com.daon.goj_gam.util.provider.ResourcesProvider

class EmptyViewHolder(
    binding: ViewholderEmptyBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider

): ModelViewHolder<Model>(binding, viewModel, resourcesProvider) {

    override fun reset() = Unit

    override fun bindViews(model: Model, adapterListener: Any) = Unit

}
package com.daon.goj_gam.widget.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.daon.goj_gam.model.Model
import com.daon.goj_gam.screen.base.BaseViewModel
import com.daon.goj_gam.util.provider.ResourcesProvider
import com.daon.goj_gam.widget.adapter.listener.AdapterListener

abstract class ModelViewHolder<M: Model>(
    binding: ViewBinding,
    protected val viewModel: BaseViewModel,
    protected val resourcesProvider: ResourcesProvider
): RecyclerView.ViewHolder(binding.root){

    abstract fun reset()

    open fun bindData(model: M) {
        reset()
    }

    abstract fun bindViews(model: M, adapterListener: AdapterListener)

}
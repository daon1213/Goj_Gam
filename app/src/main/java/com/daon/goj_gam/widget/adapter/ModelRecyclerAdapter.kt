package com.daon.goj_gam.widget.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.daon.goj_gam.model.CellType
import com.daon.goj_gam.model.Model
import com.daon.goj_gam.screen.base.BaseViewModel
import com.daon.goj_gam.util.mapper.ModelViewHolderMapper
import com.daon.goj_gam.util.provider.ResourcesProvider
import com.daon.goj_gam.widget.adapter.listener.restaurant.RestaurantListListener
import com.daon.goj_gam.widget.adapter.viewholder.ModelViewHolder

class ModelRecyclerAdapter<M: Model, VM: BaseViewModel>(
    private var modelList: List<Model>,
    private val viewModel: VM,
    private val resourceProvider: ResourcesProvider,
    private val adapterListener: RestaurantListListener
): ListAdapter<Model, ModelViewHolder<M>>(Model.DIFF_CALLBACK) {

    override fun getItemCount(): Int = modelList.size

    override fun getItemViewType(position: Int): Int = modelList[position].type.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder<M> {
        return ModelViewHolderMapper.map(parent, CellType.values()[viewType], viewModel, resourceProvider)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ModelViewHolder<M>, position: Int) {
        with(holder) {
            bindData(modelList[position] as M)
            bindViews(modelList[position] as M, adapterListener)
        }
    }

    override fun submitList(list: List<Model>?) {
        list?.let { modelList = it }
        super.submitList(list)
    }
}
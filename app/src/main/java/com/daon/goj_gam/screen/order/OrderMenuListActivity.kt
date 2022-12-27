package com.daon.goj_gam.screen.order

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.daon.goj_gam.databinding.ActivityOrderListBinding
import com.daon.goj_gam.model.food.FoodModel
import com.daon.goj_gam.screen.base.BaseActivity
import com.daon.goj_gam.util.provider.ResourcesProvider
import com.daon.goj_gam.widget.adapter.ModelRecyclerAdapter
import com.daon.goj_gam.widget.adapter.listener.order.OrderMenuListListener
import org.koin.android.ext.android.inject

class OrderMenuListActivity :
    BaseActivity<OrderMenuListViewModel, ActivityOrderListBinding>() {

    override val viewModel: OrderMenuListViewModel by inject()
    private val resourcesProvider by inject<ResourcesProvider>()

    override fun getViewBinding(): ActivityOrderListBinding =
        ActivityOrderListBinding.inflate(layoutInflater)

    private val adapter by lazy {
        ModelRecyclerAdapter<FoodModel, OrderMenuListViewModel>(
            listOf(),
            viewModel,
            adapterListener = object : OrderMenuListListener {
                override fun onRemoveItem(foodModel: FoodModel) {
                    viewModel.removeOrderMenu(foodModel)
                }
            }
        )
    }

    override fun initViews() = with(binding) {
        recyclerVIew.adapter = adapter
        toolbar.setNavigationOnClickListener { finish() }
        confirmButton.setOnClickListener {
            viewModel.orderMenu()
        }
        orderClearButton.setOnClickListener {
            viewModel.clearOrderMenu()
        }
    }

    override fun observeData() = viewModel.orderStateLiveData.observe(this) {
        when (it) {
            is OrderMenuState.Error -> handleErrorState(it)
            OrderMenuState.Loading -> handleLoadingState()
            OrderMenuState.Order -> handleOrderState()
            is OrderMenuState.Success -> handleSuccessState(it)
            OrderMenuState.UnInitialized -> Unit
        }
    }

    private fun handleErrorState(state: OrderMenuState.Error) {
        Toast.makeText(this, getString(state.messageId), Toast.LENGTH_SHORT).show()
    }

    private fun handleLoadingState() {
        binding.progressBar.isVisible = true
    }

    private fun handleSuccessState(state: OrderMenuState.Success) = with(binding) {
        progressBar.isGone = true

        val menuOrderIsEmpty = state.restaurantFoodModelList?.isEmpty() ?: true
        confirmButton.isEnabled = menuOrderIsEmpty.not()
        if (menuOrderIsEmpty) {
            Toast.makeText(
                this@OrderMenuListActivity,
                "주문 내용이 없어 화면을 종료합니다.",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        } else {
            adapter.submitList(state.restaurantFoodModelList)
        }
    }

    private fun handleOrderState() {
        // 주문이 완료된 경우
        Toast.makeText(this, "성공적으로 주문을 완료하였습니다.", Toast.LENGTH_SHORT).show()
        finish()
    }

    companion object {
        fun newInstance(context: Context) = Intent(context, OrderMenuListActivity::class.java)
    }

}
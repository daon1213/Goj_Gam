package com.daon.goj_gam.screen.main.like

import android.util.Log
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.daon.goj_gam.databinding.FragmentRestaurantLikedBinding
import com.daon.goj_gam.model.restaurant.RestaurantModel
import com.daon.goj_gam.screen.base.BaseFragment
import com.daon.goj_gam.screen.main.home.restaurant.detail.RestaurantDetailActivity
import com.daon.goj_gam.util.provider.ResourcesProvider
import com.daon.goj_gam.widget.adapter.ModelRecyclerAdapter
import com.daon.goj_gam.widget.adapter.listener.like.RestaurantLikedListListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RestaurantLikedListFragment :
    BaseFragment<RestaurantLikedListViewModel, FragmentRestaurantLikedBinding>() {

    override val viewModel: RestaurantLikedListViewModel by viewModel()
    private val resourcesProvider by inject<ResourcesProvider>()

    override fun getViewBinding(): FragmentRestaurantLikedBinding =
        FragmentRestaurantLikedBinding.inflate(layoutInflater)

    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantModel, RestaurantLikedListViewModel>(
            listOf(),
            viewModel,
            adapterListener = object : RestaurantLikedListListener {
                override fun onDislikeItem(model: RestaurantModel) {
                    viewModel.dislikeRestaurant(model.toEntity())
                }

                override fun onClickItem(model: RestaurantModel) {
                    startActivity(
                        RestaurantDetailActivity.newIntent(requireContext(), model.toEntity())
                    )
                }
            }
        )
    }

    override fun initViews() {
        super.initViews()
        binding.recyclerView.adapter = adapter
    }

    override fun observeData() = viewModel.restaurantListLiveData.observe((viewLifecycleOwner) ){
        Log.d("Restaurant", it.toString())
        checkListEmpty(it)
    }

    private fun checkListEmpty(restaurantList: List<RestaurantModel>) {
        val isEmpty = restaurantList.isEmpty()
        binding.recyclerView.isGone = isEmpty
        binding.emptyResultTextView.isVisible = isEmpty

        if (isEmpty.not()) {
            adapter.submitList(restaurantList)
        }
    }

    companion object {
        const val TAG = "RestaurantLikedListFragment"

        fun newInstance() = RestaurantLikedListFragment()
    }
}
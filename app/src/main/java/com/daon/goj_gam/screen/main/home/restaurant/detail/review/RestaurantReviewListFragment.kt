package com.daon.goj_gam.screen.main.home.restaurant.detail.review

import android.widget.Toast
import androidx.core.os.bundleOf
import com.daon.goj_gam.databinding.FragmentListBinding
import com.daon.goj_gam.model.review.ReviewModel
import com.daon.goj_gam.screen.base.BaseFragment
import com.daon.goj_gam.util.provider.ResourcesProvider
import com.daon.goj_gam.widget.adapter.ModelRecyclerAdapter
import com.daon.goj_gam.widget.adapter.listener.AdapterListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantReviewListFragment :
    BaseFragment<RestaurantReviewListViewModel, FragmentListBinding>() {

    override val viewModel: RestaurantReviewListViewModel by viewModel {
        parametersOf(
            arguments?.getString(RESTAURANT_TITLE_KEY)
        )
    }
    private val resourcesProvider by inject<ResourcesProvider>()

    override fun getViewBinding(): FragmentListBinding =
        FragmentListBinding.inflate(layoutInflater)

    private val adapter by lazy {
        ModelRecyclerAdapter<ReviewModel, RestaurantReviewListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : AdapterListener {

            }
        )
    }

    override fun initViews() {
        super.initViews()
        binding.recyclerView.adapter = adapter
    }

    override fun observeData() = viewModel.reviewStateLiveDate.observe(viewLifecycleOwner) {
        when (it) {
            RestaurantReviewState.UnInitialized -> {

            }
            RestaurantReviewState.Loading -> {

            }
            is RestaurantReviewState.Success -> {
                handleSuccess(it)
            }
            is RestaurantReviewState.Error -> {
                handleError(it)
            }
        }
    }

    private fun handleSuccess(state: RestaurantReviewState.Success) {
        adapter.submitList(state.reviewList)
    }

    private fun handleError(state: RestaurantReviewState.Error) {
        Toast.makeText(requireContext(), "리뷰 정보를 가져오는 도중 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val RESTAURANT_TITLE_KEY = "restaurantTitle"

        fun newInstance(
            restaurantTitle: String
        ): RestaurantReviewListFragment {
            val bundle = bundleOf(
                RESTAURANT_TITLE_KEY to restaurantTitle
            )
            return RestaurantReviewListFragment().apply {
                arguments = bundle
            }
        }
    }
}
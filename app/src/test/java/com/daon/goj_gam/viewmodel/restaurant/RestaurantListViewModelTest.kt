package com.daon.goj_gam.viewmodel.restaurant

import com.daon.goj_gam.ViewModelTest
import com.daon.goj_gam.data.entity.impl.LocationLatLngEntity
import com.daon.goj_gam.data.repository.restaurant.RestaurantRepository
import com.daon.goj_gam.model.restaurant.RestaurantModel
import com.daon.goj_gam.screen.main.home.restaurant.RestaurantCategory
import com.daon.goj_gam.screen.main.home.restaurant.RestaurantListViewModel
import com.daon.goj_gam.screen.main.home.restaurant.RestaurantOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.koin.core.parameter.parametersOf
import org.koin.test.inject

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
internal class RestaurantListViewModelTest: ViewModelTest() {

    private var restaurantCategory = RestaurantCategory.ALL

    private val locationLatLngEntity = LocationLatLngEntity(0.0, 0.0)

    private val restaurantListViewModel by inject<RestaurantListViewModel> {
        parametersOf(
            restaurantCategory,
            locationLatLngEntity
        )
    }

    private val restaurantRepository by inject<RestaurantRepository>()

    @Test
    fun `test load restaurant list category All`() = runBlockingTest {
        val testObservable = restaurantListViewModel.restaurantListLiveData.test()

        restaurantListViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                restaurantRepository.getList(restaurantCategory, locationLatLngEntity).map {
                    RestaurantModel(
                        id = it.id,
                        restaurantInfoId = it.restaurantInfoId,
                        restaurantCategory = it.restaurantCategory,
                        restaurantTitle = it.restaurantTitle,
                        restaurantImageUrl = it.restaurantImageUrl,
                        grade = it.grade,
                        reviewCount = it.reviewCount,
                        deliveryTimeRange = it.deliveryTimeRange,
                        deliveryTipRange = it.deliveryTipRange,
                        restaurantTelNumber = it.restaurantTelNumber
                    )
                }
            )
        )
    }

    @Test
    fun `test load restaurant list category Excepted`() = runBlockingTest {
        restaurantCategory = RestaurantCategory.CAFE_DESSERT

        val testObservable = restaurantListViewModel.restaurantListLiveData.test()

        restaurantListViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                restaurantRepository.getList(restaurantCategory, locationLatLngEntity).map {
                    RestaurantModel(
                        id = it.id,
                        restaurantInfoId = it.restaurantInfoId,
                        restaurantCategory = it.restaurantCategory,
                        restaurantTitle = it.restaurantTitle,
                        restaurantImageUrl = it.restaurantImageUrl,
                        grade = it.grade,
                        reviewCount = it.reviewCount,
                        deliveryTimeRange = it.deliveryTimeRange,
                        deliveryTipRange = it.deliveryTipRange,
                        restaurantTelNumber = it.restaurantTelNumber
                    )
                }
            )
        )
    }

    @Test
    fun `test load restaurant list order by fast delivery time`() = runBlockingTest {
        val testObservable = restaurantListViewModel.restaurantListLiveData.test()

        restaurantListViewModel.setRestaurantOrder(RestaurantOrder.FAST_DELIVERY)

        testObservable.assertValueSequence(
            listOf(
                restaurantRepository.getList(restaurantCategory, locationLatLngEntity).sortedBy { it.deliveryTimeRange.first }.map {
                    RestaurantModel(
                        id = it.id,
                        restaurantInfoId = it.restaurantInfoId,
                        restaurantCategory = it.restaurantCategory,
                        restaurantTitle = it.restaurantTitle,
                        restaurantImageUrl = it.restaurantImageUrl,
                        grade = it.grade,
                        reviewCount = it.reviewCount,
                        deliveryTimeRange = it.deliveryTimeRange,
                        deliveryTipRange = it.deliveryTipRange,
                        restaurantTelNumber = it.restaurantTelNumber
                    )
                }
            )
        )

    }

}
package com.daon.goj_gam.screen.main.home.restaurant.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.daon.goj_gam.data.entity.impl.RestaurantEntity
import com.daon.goj_gam.data.entity.impl.RestaurantFoodEntity
import com.daon.goj_gam.data.repository.food.RestaurantFoodRepository
import com.daon.goj_gam.data.repository.user.UserRepository
import com.daon.goj_gam.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantDetailViewModel(
    private val restaurantEntity: RestaurantEntity,
    private val userRepository: UserRepository,
    private val restaurantFoodRepository: RestaurantFoodRepository
): BaseViewModel() {

    private val _restaurantDetailStateLiveData = MutableLiveData<RestaurantDetailState>(RestaurantDetailState.Uninitialized)
    val restaurantDetailStateLiveData
        get() = _restaurantDetailStateLiveData

    override fun fetchData(): Job = viewModelScope.launch{
        _restaurantDetailStateLiveData.value = RestaurantDetailState.Loading
        val foods = restaurantFoodRepository.getFoods(
            restaurantId = restaurantEntity.restaurantInfoId,
            restaurantTitle = restaurantEntity.restaurantTitle
        )
        val foodMenuListInBasket = restaurantFoodRepository.getAllFoodMenuListInBasket()
        val isLiked = userRepository.getUserLikedRestaurant(restaurantEntity.restaurantTitle)
        _restaurantDetailStateLiveData.value = RestaurantDetailState.Success(
            restaurantEntity = restaurantEntity,
            restaurantFoodList = foods,
            foodMenuListInBasket = foodMenuListInBasket,
            isLiked = isLiked != null
        )
    }

    fun getRestaurantPhoneNumber(): String? {
        return when (val data = _restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                data.restaurantEntity.restaurantTelNumber
            }
            else -> null
        }
    }

    fun getRestaurantInfo(): RestaurantEntity? {
        return when (val data = _restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                data.restaurantEntity
            }
            else -> null
        }
    }

    fun toggleLikedRestaurant() = viewModelScope.launch{
        when (val data = _restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                userRepository.getUserLikedRestaurant(restaurantEntity.restaurantTitle)?.let {
                    // 이미 좋아요 한 상태
                    userRepository.deleteUserLikedRestaurant(it.restaurantTitle)
                    _restaurantDetailStateLiveData.value = data.copy(
                        isLiked = false
                    )
                } ?: run {
                    userRepository.insertUserLikedRestaurant(restaurantEntity)
                    _restaurantDetailStateLiveData.value = data.copy(
                        isLiked = true
                    )
                }
            }
        }
    }

    // 새로운 메뉴를 장바구니에 추가
    fun notifyFoodMenuListInBasket(foodEntity: RestaurantFoodEntity) = viewModelScope.launch {
        when(val data = _restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                _restaurantDetailStateLiveData.value = data.copy(
                    foodMenuListInBasket = data.foodMenuListInBasket?.toMutableList()?.apply {
                        add(foodEntity)
                    }
                )
            }
            else -> Unit
        }
    }

    // 다른 식당 메뉴 삭제 요청
    fun notifyClearNeedAlertInBasket(isClearNeed: Boolean, afterAction: () -> Unit) {
        when(val data = _restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                _restaurantDetailStateLiveData.value = data.copy(
                    isClearNeedInBasketAndAction = Pair(isClearNeed, afterAction)
                )
            }
            else -> Unit
        }
    }

    // 장바구니 지우기
    fun notifyClearBasket() = viewModelScope.launch {
        when(val data = _restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                _restaurantDetailStateLiveData.value = data.copy(
                    foodMenuListInBasket = listOf(),
                    isClearNeedInBasketAndAction = Pair(false, {})
                )
            }
            else -> Unit
        }
    }
}
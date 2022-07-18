package com.daon.goj_gam.screen.main.home.restaurant.detail.menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.daon.goj_gam.data.entity.impl.RestaurantFoodEntity
import com.daon.goj_gam.data.repository.food.RestaurantFoodRepository
import com.daon.goj_gam.model.food.FoodModel
import com.daon.goj_gam.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantMenuListViewModel(
    private val restaurantId: Long,
    private val foodEntityList: List<RestaurantFoodEntity>,
    private val restaurantFoodRepository: RestaurantFoodRepository
) : BaseViewModel() {

    private val _foodListLiveData = MutableLiveData<List<FoodModel>>()
    val foodListLiveData
        get() = _foodListLiveData

    private val _menuBasketLiveData = MutableLiveData<RestaurantFoodEntity>()
    val menuBasketLiveData
        get() = _menuBasketLiveData

    private val _isClearNeedInBasketLiveData = MutableLiveData<Pair<Boolean, () -> Unit>>()
    val isClearNeedInBasketLiveData
        get() = _isClearNeedInBasketLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        // food entity -> food model
        _foodListLiveData.value = foodEntityList.map {
            FoodModel(
                id = it.hashCode().toLong(),
                title = it.title,
                description = it.description,
                price = it.price,
                imageUrl = it.imageUrl,
                restaurantId = it.restaurantId,
                foodId = it.id,
                restaurantTitle = it.restaurantTitle
            )
        }
    }

    // 장바구니에 메뉴 추가
    fun insertMenuInBasket(foodModel: FoodModel) = viewModelScope.launch {
        val restaurantMenuListInBasket =
            restaurantFoodRepository.getFoodMenuListInBasket(restaurantId)
        val foodMenuEntity = foodModel.toEntity(restaurantMenuListInBasket.size)

        val anotherRestaurantMenuListInBasket =
            restaurantFoodRepository.getAllFoodMenuListInBasket()
                .filter { it.restaurantId != restaurantId }
        if (anotherRestaurantMenuListInBasket.isNotEmpty()) {
            // 다른 식당의 메뉴가 있는 경우
            _isClearNeedInBasketLiveData.value =
                Pair(true) { clearMenuAndInsertNewMenuInBasket(foodMenuEntity) }
        } else {
            // 다른 식당의 메뉴가 없는 경우
            restaurantFoodRepository.insertFoodMenuInBasket(foodMenuEntity)
            _menuBasketLiveData.value = foodMenuEntity
        }
    }

    private fun clearMenuAndInsertNewMenuInBasket(restaurantFoodEntity: RestaurantFoodEntity) =
        viewModelScope.launch {
            restaurantFoodRepository.clearFoodMenuListInBasket()
            restaurantFoodRepository.insertFoodMenuInBasket(restaurantFoodEntity)
            _menuBasketLiveData.value = restaurantFoodEntity
        }

}
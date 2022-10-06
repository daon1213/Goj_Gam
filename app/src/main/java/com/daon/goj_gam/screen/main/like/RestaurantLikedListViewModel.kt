package com.daon.goj_gam.screen.main.like

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.daon.goj_gam.data.entity.impl.RestaurantEntity
import com.daon.goj_gam.data.repository.user.UserRepository
import com.daon.goj_gam.model.CellType
import com.daon.goj_gam.model.restaurant.RestaurantModel
import com.daon.goj_gam.model.restaurant.toModel
import com.daon.goj_gam.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantLikedListViewModel(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _restaurantListLiveData = MutableLiveData<List<RestaurantModel>>()
    val restaurantListLiveData
        get() = _restaurantListLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        _restaurantListLiveData.value = userRepository.getAllUserLikedRestaurant().map {
            it.toModel(CellType.LIKED_RESTAURANT_CELL)
        }
    }

    fun dislikeRestaurant(restaurant: RestaurantEntity) = viewModelScope.launch {
        userRepository.deleteUserLikedRestaurant(
            restaurantTitle = restaurant.restaurantTitle
        )
        fetchData()
    }
}
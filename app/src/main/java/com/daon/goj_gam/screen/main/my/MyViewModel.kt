package com.daon.goj_gam.screen.main.my

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.daon.goj_gam.R
import com.daon.goj_gam.data.entity.impl.OrderEntity
import com.daon.goj_gam.data.preference.AppPreferenceManager
import com.daon.goj_gam.data.repository.order.DefaultOrderRepository
import com.daon.goj_gam.data.repository.order.OrderRepository
import com.daon.goj_gam.data.repository.user.UserRepository
import com.daon.goj_gam.model.order.OrderModel
import com.daon.goj_gam.screen.base.BaseViewModel
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel(
    private val preferenceManager: AppPreferenceManager,
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository
) : BaseViewModel() {

    private val _myStateLiveData = MutableLiveData<MyState>(MyState.UnInitialized)
    val myStateLiveData
        get() = _myStateLiveData

    fun saveToken(token: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            preferenceManager.putIdToken(token)
            fetchData()
        }
    }

    fun setUserInfo(firebaseUser: FirebaseUser?) = viewModelScope.launch {
        firebaseUser?.let { user ->
            when (val orderMenuResult = orderRepository.getAllOrderMenus(user.uid)) {
                is DefaultOrderRepository.Result.Error -> {
                    _myStateLiveData.value = MyState.Error(
                        R.string.error_to_order_history,
                        orderMenuResult.e
                    )
                }
                is DefaultOrderRepository.Result.Success<*> -> {
                    val orderList = orderMenuResult.data as List<OrderEntity>
                    _myStateLiveData.value = MyState.Success.Registered(
                        userName = user.displayName ?: "익명",
                        profileImageUri = user.photoUrl,
                        orderList = orderList.map {
                            OrderModel(
                                id = it.hashCode().toLong(),
                                orderId = it.id,
                                userId = it.userId,
                                restaurantId = it.restaurantId,
                                foodMenuList = it.foodMenuList,
                                restaurantTitle = it.restaurantTitle
                            )
                        }
                    )
                }
            }
        } ?: kotlin.run {
            _myStateLiveData.value = MyState.Success.NotRegistered
        }
    }

    override fun fetchData(): Job = viewModelScope.launch {
        try {
            _myStateLiveData.value = MyState.Loading
            preferenceManager.getIdToken()?.let {
                _myStateLiveData.value = MyState.Login(it)
            } ?: kotlin.run {
                _myStateLiveData.value = MyState.Success.NotRegistered
            }
        } catch (exception: Exception) {
            _myStateLiveData.value = MyState.Error(R.string.error_location_dis_found, exception)
        }
    }

    fun signOut() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            preferenceManager.removeIdToken()
        }
        userRepository.deleteAllUserLikedRestaurant()
        fetchData()
    }
}
package com.daon.goj_gam.screen.main.my

import android.net.Uri
import androidx.annotation.StringRes
import com.daon.goj_gam.model.order.OrderModel

sealed interface MyState{

    object UnInitialized: MyState

    object Loading: MyState

    data class Login(
        val idToken: String
    ): MyState

    sealed interface Success: MyState {

        data class Registered(
            val userName: String,
            val profileImageUri: Uri?,
            val orderList: List<OrderModel>
        ): Success

        object NotRegistered: Success

    }

    data class Error(
        @StringRes val messageId: Int,
        val e: Throwable
    ): MyState

}
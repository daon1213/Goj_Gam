package com.daon.goj_gam.screen.main.home

import androidx.annotation.StringRes
import com.daon.goj_gam.data.entity.MapSearchInfoEntity

sealed class HomeState {

    object Uninitialized: HomeState()

    object Loading: HomeState()

    data class Success(
        val mapSearchInfo: MapSearchInfoEntity
    ): HomeState()

    data class Error(
        @StringRes val messageId: Int
    ): HomeState()

}

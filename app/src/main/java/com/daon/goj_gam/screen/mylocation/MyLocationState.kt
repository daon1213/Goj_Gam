package com.daon.goj_gam.screen.mylocation

import androidx.annotation.StringRes
import com.daon.goj_gam.data.entity.impl.MapSearchInfoEntity

sealed class MyLocationState {

    object Uninitialized: MyLocationState()

    object Loading: MyLocationState()

    data class Success(
        val mapSearchInfoEntity: MapSearchInfoEntity
    ): MyLocationState()

    data class Confirm(
        val mapSearchInfoEntity: MapSearchInfoEntity
    ): MyLocationState()

    data class Error(
        @StringRes val messageID: Int
    ): MyLocationState()

}
package com.daon.goj_gam.screen.main.home.restaurant

import androidx.annotation.StringRes
import com.daon.goj_gam.R

enum class RestaurantCategory(
    @StringRes val categoryNameId: Int,
    @StringRes val categoryTypeId: Int
) {

    ALL(R.string.all, R.string.all_type)

}
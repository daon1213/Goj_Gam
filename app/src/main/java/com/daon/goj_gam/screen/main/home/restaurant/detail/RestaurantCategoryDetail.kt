package com.daon.goj_gam.screen.main.home.restaurant.detail

import androidx.annotation.StringRes
import com.daon.goj_gam.R

enum class RestaurantCategoryDetail(
    @StringRes val categoryNameId: Int
) {
    MENU(R.string.menu),
    REVIEW(R.string.review)
}
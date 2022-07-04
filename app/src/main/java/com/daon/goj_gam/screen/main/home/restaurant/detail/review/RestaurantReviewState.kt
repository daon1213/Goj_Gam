package com.daon.goj_gam.screen.main.home.restaurant.detail.review

import com.daon.goj_gam.model.review.ReviewModel

sealed interface RestaurantReviewState{

    object UnInitialized: RestaurantReviewState

    object Loading: RestaurantReviewState

    data class Success(
        val reviewList: List<ReviewModel>
    ): RestaurantReviewState

    data class Error(
        val exception: Throwable
    ): RestaurantReviewState

}
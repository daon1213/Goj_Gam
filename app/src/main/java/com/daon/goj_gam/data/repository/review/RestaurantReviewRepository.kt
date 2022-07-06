package com.daon.goj_gam.data.repository.review

import android.net.Uri
import com.daon.goj_gam.data.entity.impl.ReviewEntity

interface RestaurantReviewRepository {

    suspend fun getReviews(restaurantTitle: String): DefaultRestaurantReviewRepository.Result
    suspend fun insertReview(reviewEntity: ReviewEntity): DefaultRestaurantReviewRepository.Result
    suspend fun insertReviewImages(imageUrlList: List<Uri>): DefaultRestaurantReviewRepository.Result

}
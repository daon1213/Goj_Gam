package com.daon.goj_gam.screen.main.review

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.daon.goj_gam.R
import com.daon.goj_gam.data.entity.impl.ReviewEntity
import com.daon.goj_gam.data.repository.review.DefaultRestaurantReviewRepository
import com.daon.goj_gam.data.repository.review.RestaurantReviewRepository
import com.daon.goj_gam.screen.base.BaseViewModel
import kotlinx.coroutines.launch

class AddReviewViewModel(
    private val reviewRepository: RestaurantReviewRepository
): BaseViewModel() {

    private val _reviewStateLiveData = MutableLiveData<AddReviewState>(AddReviewState.UnInitialized)
    val reviewStateLiveData
        get() = _reviewStateLiveData

    fun uploadReview(
        userId: String,
        title: String,
        content: String,
        rating: Float,
        imageUrlList: List<Uri>,
        restaurantTitle: String,
        orderId: String
    ) = viewModelScope.launch {

        _reviewStateLiveData.value = AddReviewState.Loading

        // 이미지가 있다면 업로드 과정 추가
        if (imageUrlList.isNotEmpty()) {
            // 다른 thread 에서 값을 변경할 수 있는 가능성 방지
            when (val results = reviewRepository.insertReviewImages(imageUrlList)) {
                is DefaultRestaurantReviewRepository.Result.Error -> {
                    _reviewStateLiveData.value = AddReviewState.Error(
                        R.string.error_to_upload_image,
                        results.e
                    )
                }
                is DefaultRestaurantReviewRepository.Result.Success<*> -> {
                    val imagesUri = results.data as List<Any>
                    afterUploadPhoto(
                        userId, title, content, rating,
                        imagesUri, restaurantTitle, orderId
                    )
                }
            }
        } else {
            uploadReviewData(
                userId, title, content, rating,
                listOf(), restaurantTitle, orderId
            )
        }
    }

    private fun afterUploadPhoto(
        userId: String,
        title: String,
        content: String,
        rating: Float,
        imageUrlList: List<Any>,
        restaurantTitle: String,
        orderId: String
    ) {
        // error 발생한 case handle
        val errorResults = imageUrlList.filterIsInstance<Pair<Uri, Exception>>()
        val successResult = imageUrlList.filterIsInstance<String>()

        when {
            errorResults.isNotEmpty() && successResult.isNotEmpty() -> {
                // 일부 이미지만 업로드 성공
                _reviewStateLiveData.value = AddReviewState.PartialSuccess(
                    errorResults
                ){
                    uploadReviewData(
                        userId, title, content, rating,
                        successResult, restaurantTitle, orderId
                    )
                }
            }
            errorResults.isNotEmpty() && successResult.isEmpty() -> {
                // 이미지 업로드 실패
                _reviewStateLiveData.value = AddReviewState.Error(
                    R.string.error_to_upload_image,
                    Exception()
                )
            }
            else -> {
                // 모든 이미지 업로드 성공
                uploadReviewData(
                    userId, title, content, rating,
                    imageUrlList.filterIsInstance<String>(), restaurantTitle, orderId
                )
            }
        }
    }

    private fun uploadReviewData(
        userId: String,
        title: String,
        content: String,
        rating: Float,
        imageUrlList: List<String>,
        restaurantTitle: String,
        orderId: String
    ) = viewModelScope.launch {
        val review = ReviewEntity(
            id = orderId.hashCode().toLong(),
            userId = userId,
            title = title,
            createdAt = System.currentTimeMillis(),
            content = content,
            rating = rating,
            imagesUrlList = imageUrlList,
            restaurantTitle = restaurantTitle,
            orderId = orderId
        )
        when (val result = reviewRepository.insertReview(review)) {
            is DefaultRestaurantReviewRepository.Result.Error -> {
                _reviewStateLiveData.value = AddReviewState.Error(
                    R.string.error_to_upload_image,
                    result.e
                )
            }
            is DefaultRestaurantReviewRepository.Result.Success<*> -> {
                _reviewStateLiveData.value = AddReviewState.Success(
                    review
                )
            }
        }
    }
}
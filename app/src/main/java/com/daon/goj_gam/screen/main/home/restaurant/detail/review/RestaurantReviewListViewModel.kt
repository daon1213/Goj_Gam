package com.daon.goj_gam.screen.main.home.restaurant.detail.review

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.daon.goj_gam.data.entity.impl.ReviewEntity
import com.daon.goj_gam.data.repository.review.DefaultRestaurantReviewRepository
import com.daon.goj_gam.data.repository.review.RestaurantReviewRepository
import com.daon.goj_gam.model.review.ReviewModel
import com.daon.goj_gam.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantReviewListViewModel(
    private val restaurantTitle: String,
    private val restaurantReviewRepository: RestaurantReviewRepository
): BaseViewModel() {

    private val _reviewStateLiveData = MutableLiveData<RestaurantReviewState>(RestaurantReviewState.UnInitialized)
    val reviewStateLiveData
        get() = _reviewStateLiveData

    override fun fetchData(): Job = viewModelScope.launch{
        _reviewStateLiveData.value = RestaurantReviewState.Loading
        when(val reviews = restaurantReviewRepository.getReviews(restaurantTitle)) {
            is DefaultRestaurantReviewRepository.Result.Error -> {
                _reviewStateLiveData.value = RestaurantReviewState.Error(
                    reviews.e
                )
            }
            is DefaultRestaurantReviewRepository.Result.Success<*> -> {
                _reviewStateLiveData.value = RestaurantReviewState.Success(
                    (reviews.data as List<ReviewEntity>).map {
                        ReviewModel(
                            id = it.id,
                            title = it.title,
                            description = it.content,
                            grade = it.rating.toInt(),
                            thumbnailImageUri = if (it.imagesUrlList?.isNotEmpty() ?: false) {
                                Uri.parse(it.imagesUrlList?.first())
                            } else {
                                null
                            }
                        )
                    }
                )
            }
        }
    }

}
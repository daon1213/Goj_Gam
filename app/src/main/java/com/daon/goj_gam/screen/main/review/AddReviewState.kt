package com.daon.goj_gam.screen.main.review

import android.net.Uri
import androidx.annotation.StringRes
import com.daon.goj_gam.data.entity.impl.ReviewEntity

sealed interface AddReviewState{

    object UnInitialized: AddReviewState

    object Loading: AddReviewState

    data class Success(
        val review: ReviewEntity
    ): AddReviewState

    data class PartialSuccess(
        val errorResults: List<Pair<Uri, Exception>>,
        val cb: () -> Unit
    ): AddReviewState

    data class Error(
        @StringRes val messageId: Int,
        val error: Throwable
    ): AddReviewState
}
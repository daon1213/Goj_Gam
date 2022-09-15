package com.daon.goj_gam.screen.main.review.gallery

import com.daon.goj_gam.model.photo.PhotoModel

sealed class GalleryState {
    object Unintialized: GalleryState()

    object Loading: GalleryState()

    data class Success(
        val photoList: List<PhotoModel>
    ): GalleryState()

    data class Confirm(
        val photoList: List<PhotoModel>
    ): GalleryState()

    object Error: GalleryState()
}
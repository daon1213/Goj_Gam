package com.daon.goj_gam.data.repository.photo

import com.daon.goj_gam.model.photo.PhotoModel

interface GalleryRepository {
    suspend fun getAllPhoto(): MutableList<PhotoModel>
}
package com.daon.goj_gam.screen.main.review.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.daon.goj_gam.data.repository.photo.GalleryRepository
import com.daon.goj_gam.model.photo.PhotoModel
import com.daon.goj_gam.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class GalleryViewModel(
    private val galleryRepository: GalleryRepository
) : BaseViewModel() {

    private val _galleryStateLiveData = MutableLiveData<GalleryState>(GalleryState.Unintialized)
    val galleryStateLiveData
        get() = _galleryStateLiveData

    private lateinit var photoList: MutableList<PhotoModel>

    override fun fetchData(): Job = viewModelScope.launch {
        _galleryStateLiveData.value = GalleryState.Loading
        try {
            photoList = galleryRepository.getAllPhoto()
            _galleryStateLiveData.value = GalleryState.Success(
                photoList
            )
        } catch (exception: Exception) {
            _galleryStateLiveData.value = GalleryState.Error
        }
    }

    fun selectPhoto(photoModel: PhotoModel) {
        val findGalleryPhoto = photoList.find { it.id == photoModel.id }

        findGalleryPhoto?.let { photo ->
            photoList[photoList.indexOf(photo)] =
                photo.copy(
                    isSelected = photo.isSelected.not()
                )
            _galleryStateLiveData.value = GalleryState.Success(
                photoList
            )
        }
    }

    fun confirmCheckedPhoto() {
        _galleryStateLiveData.value = GalleryState.Loading
        _galleryStateLiveData.value = GalleryState.Confirm(
            photoList = photoList.filter { it.isSelected }
        )
    }
}
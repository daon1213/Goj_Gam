package com.daon.goj_gam.model.photo

import android.net.Uri

data class PhotoModel(
    val id: Long,
    val uri: Uri,
    val name: String,
    val date: String,
    val size: Int,
    var isSelected: Boolean = false
)
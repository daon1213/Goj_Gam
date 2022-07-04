package com.daon.goj_gam.model.review

import android.net.Uri
import com.daon.goj_gam.model.CellType
import com.daon.goj_gam.model.Model

// for recycler view model
data class ReviewModel(
    override val id: Long,
    override val type: CellType = CellType.REVIEW_CELL,
    val title: String,
    val description: String,
    val grade: Int,
    val thumbnailImageUri: Uri? = null
): Model(id, type)
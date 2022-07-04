package com.daon.goj_gam.data.entity.impl

import com.daon.goj_gam.data.entity.Entity

data class ReviewEntity(
    override val id: Long,
    val userId: String,
    val title: String,
    val createdAt: Long,
    val content: String,
    val rating: Float,
    val imagesUrlList: List<String>? = null,
    val orderId: String,
    val restaurantTitle: String
): Entity
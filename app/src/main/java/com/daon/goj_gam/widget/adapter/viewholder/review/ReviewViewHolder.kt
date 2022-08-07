package com.daon.goj_gam.widget.adapter.viewholder.review

import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.daon.goj_gam.databinding.ViewholderReviewBinding
import com.daon.goj_gam.extension.clear
import com.daon.goj_gam.extension.load
import com.daon.goj_gam.model.review.ReviewModel
import com.daon.goj_gam.screen.base.BaseViewModel
import com.daon.goj_gam.util.provider.ResourcesProvider
import com.daon.goj_gam.widget.adapter.viewholder.ModelViewHolder

class ReviewViewHolder(
    private val binding: ViewholderReviewBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
): ModelViewHolder<ReviewModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        reviewThumbnailImage.clear()
        reviewThumbnailImage.isGone = true
    }

    override fun bindData(model: ReviewModel){
        super.bindData(model)
        with(binding) {
            reviewTitleText.text = model.title
            ratingBar.rating = model.grade.toFloat()
            reviewText.text = model.description

            if (model.thumbnailImageUri != null) {
                reviewThumbnailImage.isVisible = true
                reviewThumbnailImage.load(model.thumbnailImageUri.toString(), 24f)
            } else {
                reviewThumbnailImage.isGone = true
            }
        }
    }

    override fun bindViews(model: ReviewModel, adapterListener: Any) = Unit

}
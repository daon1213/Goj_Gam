package com.daon.goj_gam.screen.main.review.gallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.daon.goj_gam.R
import com.daon.goj_gam.databinding.ActivityGalleryBinding
import com.daon.goj_gam.screen.base.BaseActivity
import com.daon.goj_gam.util.decorate.GridDividerDecoration
import com.daon.goj_gam.widget.adapter.GalleryPhotoListAdapter
import org.koin.android.ext.android.inject

class GalleryActivity : BaseActivity<GalleryViewModel, ActivityGalleryBinding>() {

    override val viewModel: GalleryViewModel by inject()

    override fun getViewBinding(): ActivityGalleryBinding =
        ActivityGalleryBinding.inflate(layoutInflater)

    private val photoAdapter by lazy {
        GalleryPhotoListAdapter{ photoModel ->
            viewModel.selectPhoto(photoModel)
        }
    }

    override fun initViews() = with(binding) {
        recyclerView.adapter = photoAdapter
        recyclerView.addItemDecoration(
            GridDividerDecoration(this@GalleryActivity, R.drawable.bg_frame_gallery)
        )
        confirmButton.setOnClickListener {
            viewModel.confirmCheckedPhoto()
        }
    }

    override fun observeData() = viewModel.galleryStateLiveData.observe(this) {
        when (it) {
            GalleryState.Unintialized -> Unit
            GalleryState.Loading -> handleLoadingState()
            is GalleryState.Success -> handleSuccessState(it)
            is GalleryState.Confirm -> handleConfirmState(it)
            GalleryState.Error -> handleErrorState()
        }
    }

    private fun handleLoadingState() = with(binding) {
        progressBar.isVisible = true
        recyclerView.isGone = true
    }

    private fun handleSuccessState(state: GalleryState.Success) = with(binding) {
        progressBar.isGone = true
        recyclerView.isVisible = true
        photoAdapter.setPhotoList(state.photoList)
    }

    private fun handleConfirmState(state: GalleryState.Confirm) {
        setResult(
            Activity.RESULT_OK,
            Intent().apply {
                putExtra(URI_LIST_KEY, ArrayList(state.photoList.map { it.uri }))
            }
        )
        finish()
    }

    private fun handleErrorState() {
        binding.progressBar.isGone = true
        Toast.makeText(
            this,
            "갤러리에서 이미지를 가져오는 도중 문제가 발생하였습니다.",
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        // 갤러리에서 사진 선택
        const val URI_LIST_KEY = "uriList"

        fun newInstance(context: Context) = Intent(context, GalleryActivity::class.java)
    }
}
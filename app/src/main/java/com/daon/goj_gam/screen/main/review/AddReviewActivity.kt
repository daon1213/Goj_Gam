package com.daon.goj_gam.screen.main.review

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.daon.goj_gam.R
import com.daon.goj_gam.databinding.ActivityAddReviewBinding
import com.daon.goj_gam.screen.base.BaseActivity
import com.daon.goj_gam.screen.main.review.gallery.GalleryActivity
import com.daon.goj_gam.screen.main.review.gallery.GalleryActivity.Companion.URI_LIST_KEY
import com.daon.goj_gam.widget.adapter.PhotoListAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddReviewActivity : BaseActivity<AddReviewViewModel, ActivityAddReviewBinding>() {

    private val restaurantTitle by lazy { intent.getStringExtra(RESTAURANT_TITLE).orEmpty() }
    private val orderId by lazy { intent.getStringExtra(ORDER_ID).orEmpty() }

    override val viewModel: AddReviewViewModel by viewModel()
    private val auth: FirebaseAuth by inject()
    private val storage: FirebaseStorage by inject()

    // 선택한 이미지 리스트
    private var imageUrlList: ArrayList<Uri> = arrayListOf()

    private val photoListAdapter = PhotoListAdapter { uri -> removePhoto(uri) }

    private val galleryLauncher = // 갤러리에서 사진 선택
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let { intent ->
                    val urlList = intent.getParcelableArrayListExtra<Uri>(URI_LIST_KEY)
                    urlList?.let { list ->
                        imageUrlList.addAll(list)
                        photoListAdapter.setPhotoList(imageUrlList)
                    }
                } ?: kotlin.run {
                    Toast.makeText(this, "사진을 가져오는 도중 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    private val cameraLauncher = // 카메라로 사진 촬영
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let { intent ->
                    val uriList = intent.getParcelableArrayListExtra<Uri>(URI_LIST_KEY)
                    uriList?.let { list ->
                        imageUrlList.addAll(list)
                        // RecyclerView Adapter
                        photoListAdapter.setPhotoList(imageUrlList)
                    }
                } ?: kotlin.run {
                    Toast.makeText(this, "사진을 가져오는 도중 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun getViewBinding(): ActivityAddReviewBinding =
        ActivityAddReviewBinding.inflate(layoutInflater)

    override fun initViews() = with(binding) {

        toolbar.setNavigationOnClickListener {
            finish()
        }

        photoRecyclerView.adapter = photoListAdapter
        titleTextView.text = getString(R.string.add_restaurant_review, restaurantTitle)

        // 이미지 추가 버튼
        imageAddButton.setOnClickListener {
            showPictureUploadDialog()
        }

        // 리뷰 등록 버튼
        submitButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            val userId = auth.currentUser?.uid.orEmpty()
            val rating = binding.ratingBar.rating

            if (userId.isNullOrBlank()) {
                Toast.makeText(this@AddReviewActivity, "로그인 해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.uploadReview(
                userId, title, content, rating,
                imageUrlList, restaurantTitle, orderId
            )
        }
    }

    private fun showPictureUploadDialog() {
        AlertDialog.Builder(this)
            .setTitle("사진첨부")
            .setMessage("사진첨부할 방식을 선택해주세요.")
//            .setPositiveButton("카메라") { _, _ ->
//                checkExternalStoragePermission {
//                    startCameraScreen()
//                }
//            }
            .setNegativeButton("갤러리") { _, _ ->
                checkExternalStoragePermission {
                    startGalleryScreen()
                }
            }
            .create()
            .show()
    }

    private fun startGalleryScreen() {
        galleryLauncher.launch(
            GalleryActivity.newInstance(this)
        )
    }

//    private fun startCameraScreen() {
//        cameraLauncher.launch(
//            CameraActivity.newInstance(this)
//        )
//    }

    private fun checkExternalStoragePermission(uploadAction: () -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                uploadAction()
            }
            shouldShowRequestPermissionRationale(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                showPermissionContextPopup()
            }
            else -> {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("사진을 가져오기 위해 필요합니다.")
            .setPositiveButton("동의") { _, _ ->
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
            .create()
            .show()
    }

    override fun observeData() = viewModel.reviewStateLiveData.observe(this){
        when(it) {
            AddReviewState.UnInitialized -> Unit
            AddReviewState.Loading -> handleLoadingState()
            is AddReviewState.PartialSuccess -> {
                handlePartialSuccessState(it.errorResults, it.cb)
            }
            is AddReviewState.Success -> TODO()
            is AddReviewState.Error -> handleErrorState(it)
        }
    }

    private fun handleLoadingState() = with(binding) {
        progressBar.isVisible = true
    }

    private fun handlePartialSuccessState(
        errorResults: List<Pair<Uri, Exception>>,
        cb: () -> Unit
    ) {
        AlertDialog.Builder(this)
            .setTitle("특정 이미지 업로드 실패")
            .setMessage("업로드에 실패한 이미지가 있습니다." + errorResults.map { (uri, _) ->
                "$uri\n"
            } + "업로드를 진행하시겠습니까?")
            .setPositiveButton("업로드") {_,_ ->
                cb()
            }
            .create()
            .show()
    }

    private fun handleErrorState(state: AddReviewState.Error) {
        binding.progressBar.isGone = true
        Snackbar.make(binding.root, getText(state.messageId), Snackbar.LENGTH_SHORT).show()
    }

    private fun removePhoto(uri: Uri) {
        imageUrlList.remove(uri)
        photoListAdapter.setPhotoList(imageUrlList)
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1000
        private const val RESTAURANT_TITLE = "RESTAURANT_TITLE"
        private const val ORDER_ID = "ORDER_ID"

        fun newInstance(context: Context, restaurantTitle: String, orderId: String) =
            Intent(context, AddReviewActivity::class.java).apply {
                putExtra(RESTAURANT_TITLE, restaurantTitle)
                putExtra(ORDER_ID, orderId)
            }
    }
}
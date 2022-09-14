package com.daon.goj_gam.widget.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.daon.goj_gam.R
import com.daon.goj_gam.databinding.ItemGalleryBinding
import com.daon.goj_gam.extension.load
import com.daon.goj_gam.model.photo.PhotoModel

class GalleryPhotoListAdapter(
    private val checkPhotoListener: (PhotoModel) -> Unit
): RecyclerView.Adapter<GalleryPhotoListAdapter.PhotoViewHolder>() {

    private var galleryPhotoList: List<PhotoModel> = emptyList()

    inner class PhotoViewHolder(
        private val binding: ItemGalleryBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bindData(model: PhotoModel) = with(binding) {
            photoImageView.load(model.uri.toString(), 8f, CenterCrop())
            checkButton.setImageDrawable(
                ContextCompat.getDrawable(
                    root.context,
                    if (model.isSelected)
                        R.drawable.ic_check_enabled
                    else
                        R.drawable.ic_check_disabled
                )
            )
        }

        fun bindView(model: PhotoModel) {
            binding.root.setOnClickListener {
                checkPhotoListener(model)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            ItemGalleryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bindData(galleryPhotoList[position])
        holder.bindView(galleryPhotoList[position])
    }

    override fun getItemCount() = galleryPhotoList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setPhotoList(galleryPhotoList: List<PhotoModel>) {
        this.galleryPhotoList = galleryPhotoList
        notifyDataSetChanged()
    }
}
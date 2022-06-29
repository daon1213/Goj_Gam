package com.daon.goj_gam.data.entity.impl

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.daon.goj_gam.data.entity.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@androidx.room.Entity
data class LocationLatLngEntity (
    val latitude: Double,
    val longitude: Double,
    @PrimaryKey(autoGenerate = true)
    override val id: Long = -1
): Entity, Parcelable
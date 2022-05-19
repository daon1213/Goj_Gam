package com.daon.goj_gam.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationLatLngEntity (
    val latitude: Double,
    val longitude: Double,
    override val id: Long = -1
): Entity, Parcelable
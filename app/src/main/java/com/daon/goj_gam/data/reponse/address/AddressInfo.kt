package com.daon.goj_gam.data.reponse.address

import com.daon.goj_gam.data.entity.LocationLatLngEntity
import com.daon.goj_gam.data.entity.MapSearchInfoEntity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddressInfo(
    @SerializedName("fullAddress")
    @Expose
    val fullAddress: String?,
    @SerializedName("addressKey")
    @Expose
    val addressKey: String?,
    @SerializedName("roadAddressKey")
    @Expose
    val roadAddressKey: String?,
    @SerializedName("addressType")
    @Expose
    val addressType: String?,
    @SerializedName("city_do")
    @Expose
    val cityDo: String?,
    @SerializedName("gu_gun")
    @Expose
    val guGun: String?,
    @SerializedName("eup_myun")
    @Expose
    val eupMyun: String?,
    @SerializedName("adminDong")
    @Expose
    val adminDong: String?,
    @SerializedName("adminDongCode")
    @Expose
    val adminDongCode: String?,
    @SerializedName("legalDong")
    @Expose
    val legalDong: String?,
    @SerializedName("legalDongCode")
    @Expose
    val legalDongCode: String?,
    @SerializedName("ri")
    @Expose
    val ri: String?,
    @SerializedName("roadName")
    @Expose
    val roadName: String?,
    @SerializedName("buildingIndex")
    @Expose
    val buildingIndex: String?,
    @SerializedName("buildingName")
    @Expose
    val buildingName: String?,
    @SerializedName("mappingDistance")
    @Expose
    val mappingDistance: String?,
    @SerializedName("roadCode")
    @Expose
    val roadCode: String?
) {
    fun toSearchInfoEntity(
        locationLatLngEntity: LocationLatLngEntity
    ) =
        MapSearchInfoEntity(
            fullAddress = fullAddress ?: "위치 정보 없음",
            name = buildingName ?: "빌딩 정보 없음",
            locationLatLng = locationLatLngEntity
        )
}
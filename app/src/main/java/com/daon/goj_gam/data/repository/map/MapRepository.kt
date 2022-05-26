package com.daon.goj_gam.data.repository.map

import com.daon.goj_gam.data.entity.LocationLatLngEntity
import com.daon.goj_gam.data.reponse.address.AddressInfo

interface MapRepository {

    suspend fun getReverseGeoInformation(
        locationLatLngEntity: LocationLatLngEntity
    ): AddressInfo?

}
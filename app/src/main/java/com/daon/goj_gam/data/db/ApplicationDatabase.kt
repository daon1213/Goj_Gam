package com.daon.goj_gam.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.daon.goj_gam.data.db.dao.FoodMenuBasketDao
import com.daon.goj_gam.data.db.dao.LocationDao
import com.daon.goj_gam.data.db.dao.RestaurantDao
import com.daon.goj_gam.data.entity.impl.LocationLatLngEntity
import com.daon.goj_gam.data.entity.impl.RestaurantEntity
import com.daon.goj_gam.data.entity.impl.RestaurantFoodEntity

@Database(
    entities = [LocationLatLngEntity::class, RestaurantEntity::class, RestaurantFoodEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase: RoomDatabase() {

    companion object {
        const val DB_NAME = "ApplicationDataBase.db"
    }

    abstract fun LocationDao(): LocationDao

    abstract fun RestaurantDao(): RestaurantDao

    abstract fun FoodMenuBasketDao(): FoodMenuBasketDao
}
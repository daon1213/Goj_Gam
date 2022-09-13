package com.daon.goj_gam.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.daon.goj_gam.data.entity.impl.RestaurantFoodEntity

@Dao
interface FoodMenuBasketDao {

    @Query("SELECT * FROM RestaurantFoodEntity WHERE id = :foodId AND restaurantId = :restaurantId")
    suspend fun get(restaurantId: Long, foodId: Long): List<RestaurantFoodEntity>

    @Query("SELECT * FROM RestaurantFoodEntity")
    suspend fun getAll(): List<RestaurantFoodEntity>

    @Query("SELECT * FROM RestaurantFoodEntity WHERE restaurantId = :restaurantId")
    suspend fun getAllByRestaurantId(restaurantId: Long): List<RestaurantFoodEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(restaurantFoodEntity: RestaurantFoodEntity)

    @Query("DELETE FROM RestaurantFoodEntity WHERE id = :foodId")
    suspend fun delete(foodId: String)

    @Query("DELETE FROM RestaurantFoodEntity")
    suspend fun deleteAll()

}
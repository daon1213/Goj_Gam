package com.daon.goj_gam.data.repository.food

import com.daon.goj_gam.data.db.dao.FoodMenuBasketDao
import com.daon.goj_gam.data.entity.impl.RestaurantFoodEntity
import com.daon.goj_gam.data.network.FoodApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultRestaurantFoodRepository(
    private val foodApiService: FoodApiService,
    private val foodMenuBasketDao: FoodMenuBasketDao,
    private val ioDispatcher: CoroutineDispatcher
) : RestaurantFoodRepository {
    override suspend fun getFoods(
        restaurantId: Long,
        restaurantTitle: String
    ): List<RestaurantFoodEntity> =
        withContext(ioDispatcher) {
            val response = foodApiService.getRestaurantFoods(restaurantId)

            if (response.isSuccessful) {
                response.body()?.map {
                    it.toEntity(restaurantId, restaurantTitle)
                } ?: listOf()
            } else {
                listOf()
            }
        }

    override suspend fun getAllFoodMenuListInBasket(): List<RestaurantFoodEntity> =
        withContext(ioDispatcher) {
            foodMenuBasketDao.getAll()
        }

    override suspend fun getFoodMenuListInBasket(restaurantId: Long): List<RestaurantFoodEntity> =
        withContext(ioDispatcher) {
            foodMenuBasketDao.getAllByRestaurantId(restaurantId)
        }

    override suspend fun insertFoodMenuInBasket(restaurantFoodEntity: RestaurantFoodEntity) =
        withContext(ioDispatcher) {
            foodMenuBasketDao.insert(restaurantFoodEntity)
        }

    override suspend fun removeFoodMenuListInBasket(foodId: String) =
        withContext(ioDispatcher) {
            foodMenuBasketDao.delete(foodId = foodId)
        }

    override suspend fun clearFoodMenuListInBasket() =
        withContext(ioDispatcher) {
            foodMenuBasketDao.deleteAll()
        }
}
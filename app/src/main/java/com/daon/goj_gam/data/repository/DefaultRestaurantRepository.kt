package com.daon.goj_gam.data.repository

import com.daon.goj_gam.data.entity.RestaurantEntity
import com.daon.goj_gam.screen.main.home.restaurant.RestaurantCategory
import com.daon.goj_gam.util.provider.ResourcesProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultRestaurantRepository(
    private val resourcesProvider: ResourcesProvider,
    private val ioDispatcher: CoroutineDispatcher
): RestaurantRepository {

    override suspend fun getList(
        restaurantCategory: RestaurantCategory
    ): List<RestaurantEntity> = withContext(ioDispatcher) {

        listOf(
            RestaurantEntity(
                id = 0,
                restaurantInfold = 0,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantTitle = "마포화로집",
                restaurantImageUrl = "https://picsum.photos/200",
                grade = (1 until 5).random() + ((0..10).random() / 10f),
                reviewCount = (0 until 200).random(),
                deliveryTimeRange = Pair(0, 20),
                deliveryTipRange = Pair(0, 2000)
            ),
            RestaurantEntity(
                id = 1,
                restaurantInfold = 1,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantTitle = "옛날우동&덮밥",
                restaurantImageUrl = "https://picsum.photos/200",
                grade = (1 until 5).random() + ((0..10).random() / 10f),
                reviewCount = (0 until 200).random(),
                deliveryTimeRange = Pair(0, 20),
                deliveryTipRange = Pair(0, 2000)
            ),
            RestaurantEntity(
                id = 2,
                restaurantInfold = 2,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantTitle = "마스터석쇠불고기&냉면plus",
                restaurantImageUrl = "https://picsum.photos/200",
                grade = (1 until 5).random() + ((0..10).random() / 10f),
                reviewCount = (0 until 200).random(),
                deliveryTimeRange = Pair(0, 20),
                deliveryTipRange = Pair(0, 2000)
            ),
            RestaurantEntity(
                id = 3,
                restaurantInfold = 3,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantTitle = "마스터통삼겹",
                restaurantImageUrl = "https://picsum.photos/200",
                grade = (1 until 5).random() + ((0..10).random() / 10f),
                reviewCount = (0 until 200).random(),
                deliveryTimeRange = Pair(0, 20),
                deliveryTipRange = Pair(0, 2000)
            ),
            RestaurantEntity(
                id = 4,
                restaurantInfold = 4,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantTitle = "창영이 족발&보쌈",
                restaurantImageUrl = "https://picsum.photos/200",
                grade = (1 until 5).random() + ((0..10).random() / 10f),
                reviewCount = (0 until 200).random(),
                deliveryTimeRange = Pair(0, 20),
                deliveryTipRange = Pair(0, 2000)
            ),
            RestaurantEntity(
                id = 5,
                restaurantInfold = 5,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantTitle = "콩나물국밥&코다리조림 콩심 인천논현점",
                restaurantImageUrl = "https://picsum.photos/200",
                grade = (1 until 5).random() + ((0..10).random() / 10f),
                reviewCount = (0 until 200).random(),
                deliveryTimeRange = Pair(0, 20),
                deliveryTipRange = Pair(0, 2000)
            ),
            RestaurantEntity(
                id = 6,
                restaurantInfold = 6,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantTitle = "김여사 칼국수&냉면 논현점",
                restaurantImageUrl = "https://picsum.photos/200",
                grade = (1 until 5).random() + ((0..10).random() / 10f),
                reviewCount = (0 until 200).random(),
                deliveryTimeRange = Pair(0, 20),
                deliveryTipRange = Pair(0, 2000)
            ),
            RestaurantEntity(
                id = 7,
                restaurantInfold = 7,
                restaurantCategory = RestaurantCategory.ALL,
                restaurantTitle = "돈키호테",
                restaurantImageUrl = "https://picsum.photos/200",
                grade = (1 until 5).random() + ((0..10).random() / 10f),
                reviewCount = (0 until 200).random(),
                deliveryTimeRange = Pair(0, 20),
                deliveryTipRange = Pair(0, 2000)
            ),
        )

    }
}
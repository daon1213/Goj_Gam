package com.daon.goj_gam.di

import com.daon.goj_gam.data.entity.impl.LocationLatLngEntity
import com.daon.goj_gam.data.entity.impl.MapSearchInfoEntity
import com.daon.goj_gam.data.entity.impl.RestaurantEntity
import com.daon.goj_gam.data.entity.impl.RestaurantFoodEntity
import com.daon.goj_gam.data.repository.food.DefaultRestaurantFoodRepository
import com.daon.goj_gam.data.repository.food.RestaurantFoodRepository
import com.daon.goj_gam.data.repository.map.DefaultMapRepository
import com.daon.goj_gam.data.repository.map.MapRepository
import com.daon.goj_gam.data.repository.restaurant.DefaultRestaurantRepository
import com.daon.goj_gam.data.repository.restaurant.RestaurantRepository
import com.daon.goj_gam.data.repository.review.DefaultRestaurantReviewRepository
import com.daon.goj_gam.data.repository.review.RestaurantReviewRepository
import com.daon.goj_gam.data.repository.user.DefaultUserRepository
import com.daon.goj_gam.data.repository.user.UserRepository
import com.daon.goj_gam.screen.main.home.HomeViewModel
import com.daon.goj_gam.screen.main.home.restaurant.RestaurantCategory
import com.daon.goj_gam.screen.main.home.restaurant.RestaurantListViewModel
import com.daon.goj_gam.screen.main.home.restaurant.detail.RestaurantDetailViewModel
import com.daon.goj_gam.screen.main.home.restaurant.detail.menu.RestaurantMenuListViewModel
import com.daon.goj_gam.screen.main.home.restaurant.detail.review.RestaurantReviewListViewModel
import com.daon.goj_gam.screen.main.my.MyViewModel
import com.daon.goj_gam.screen.mylocation.MyLocationViewModel
import com.daon.goj_gam.util.provider.DefaultResourcesProvider
import com.daon.goj_gam.util.provider.ResourcesProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { MyViewModel() }
    viewModel { (restaurantCategory: RestaurantCategory, locationLatLng: LocationLatLngEntity) ->
        RestaurantListViewModel(restaurantCategory,
            locationLatLng,
            get())
    }
    viewModel { (mapSearchInfoEntity: MapSearchInfoEntity) ->
        MyLocationViewModel(mapSearchInfoEntity,
            get(),
            get())
    }
    viewModel { (restaurantEntity: RestaurantEntity) ->
        RestaurantDetailViewModel(restaurantEntity,
            get(),
            get())
    }

    viewModel { (restaurantId: Long, foodList: List<RestaurantFoodEntity>) ->
        RestaurantMenuListViewModel(restaurantId, foodList, get())
    }
    viewModel { (restaurantTitle: String) -> RestaurantReviewListViewModel(restaurantTitle, get()) }

    viewModel { (restaurantId: Long, restaurantFoodList: List<RestaurantFoodEntity>) ->
        RestaurantMenuListViewModel(restaurantId, restaurantFoodList, get())
    }
    viewModel { (restaurantTitle : String) -> RestaurantReviewListViewModel(restaurantTitle, get()) }

    single<RestaurantRepository> { DefaultRestaurantRepository(get(), get(), get()) }
    single<MapRepository> { DefaultMapRepository(get(), get()) }
    single<UserRepository> { DefaultUserRepository(get(), get(), get()) }
    single<RestaurantFoodRepository> { DefaultRestaurantFoodRepository(get(), get(), get()) }
    single<RestaurantReviewRepository> { DefaultRestaurantReviewRepository(get(), get(), get())}

    single { providerGsonConvertFactory() }
    single { buildOkHttpClient() }

    single(named("map")) { provideMapRetrofit(get(), get()) }
    single(named("food")) { provideFoodRetrofit(get(), get()) }

    single { provideMapApiService(get(qualifier = named("map"))) }
    single { provideFoodApiService(get(qualifier = named("food"))) }

    single { provideDB(androidApplication()) }
    single { provideLocationDao(get()) }
    single { provideRestaurantDao(get()) }
    single { provideFoodMenuBasketDao(get()) }

    single<ResourcesProvider> { DefaultResourcesProvider(androidApplication()) }

    single { Dispatchers.IO }
    single { Dispatchers.Main }


}
package com.daon.goj_gam.di


import com.daon.goj_gam.data.entity.LocationLatLngEntity
import com.daon.goj_gam.data.entity.MapSearchInfoEntity
import com.daon.goj_gam.data.entity.RestaurantEntity
import com.daon.goj_gam.data.repository.map.DefaultMapRepository
import com.daon.goj_gam.data.repository.map.MapRepository
import com.daon.goj_gam.data.repository.restaurant.DefaultRestaurantRepository
import com.daon.goj_gam.data.repository.restaurant.RestaurantRepository
import com.daon.goj_gam.data.repository.user.DefaultUserRepository
import com.daon.goj_gam.data.repository.user.UserRepository
import com.daon.goj_gam.screen.main.home.HomeViewModel
import com.daon.goj_gam.screen.main.home.restaurant.RestaurantCategory
import com.daon.goj_gam.screen.main.home.restaurant.RestaurantListViewModel
import com.daon.goj_gam.screen.main.home.restaurant.detail.RestaurantDetailViewModel
import com.daon.goj_gam.screen.main.my.MyViewModel
import com.daon.goj_gam.screen.mylocation.MyLocationViewModel
import com.daon.goj_gam.util.provider.DefaultResourcesProvider
import com.daon.goj_gam.util.provider.ResourcesProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { HomeViewModel(get(), get()) }
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
    viewModel { (restaurantEntity: RestaurantEntity) -> RestaurantDetailViewModel(restaurantEntity, get()) }

    single<RestaurantRepository> { DefaultRestaurantRepository(get(), get(), get()) }
    single<MapRepository> { DefaultMapRepository(get(), get()) }
    single<UserRepository> { DefaultUserRepository(get(), get(), get()) }

    single { providerGsonConvertFactory() }
    single { buildOkHttpClient() }

    single { provideRetrofit(get(), get()) }

    single { provideMapApiService(get()) }

    single { provideDB(androidApplication()) }
    single { provideLocationDao(get()) }
    single { provideRestaurantDao(get()) }

    single<ResourcesProvider> { DefaultResourcesProvider(androidApplication()) }

    single { Dispatchers.IO }
    single { Dispatchers.Main }


}
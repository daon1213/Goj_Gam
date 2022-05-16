package com.daon.goj_gam.di


import com.daon.goj_gam.data.repository.map.DefaultMapRepository
import com.daon.goj_gam.data.repository.map.MapRepository
import com.daon.goj_gam.data.repository.restaurant.DefaultRestaurantRepository
import com.daon.goj_gam.data.repository.restaurant.RestaurantRepository
import com.daon.goj_gam.screen.main.home.HomeViewModel
import com.daon.goj_gam.screen.main.home.restaurant.RestaurantCategory
import com.daon.goj_gam.screen.main.home.restaurant.RestaurantListViewModel
import com.daon.goj_gam.screen.main.my.MyViewModel
import com.daon.goj_gam.util.provider.DefaultResourcesProvider
import com.daon.goj_gam.util.provider.ResourcesProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { HomeViewModel() }
    viewModel { MyViewModel() }
    viewModel { (restaurantCategory: RestaurantCategory) -> RestaurantListViewModel(restaurantCategory, get()) }

    single <RestaurantRepository> { DefaultRestaurantRepository(get(), get()) }
    single <MapRepository> { DefaultMapRepository(get(), get()) }

    single { providerGsonConvertFactory() }
    single { buildOkHttpClient() }

    single { provideRetrofit(get(), get()) }

    single<ResourcesProvider> {DefaultResourcesProvider(androidApplication())}

    single { Dispatchers.IO }
    single { Dispatchers.Main }

}
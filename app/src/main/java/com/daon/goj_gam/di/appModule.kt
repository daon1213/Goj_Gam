package com.daon.goj_gam.di

import com.daon.goj_gam.data.entity.impl.LocationLatLngEntity
import com.daon.goj_gam.data.entity.impl.MapSearchInfoEntity
import com.daon.goj_gam.data.entity.impl.RestaurantEntity
import com.daon.goj_gam.data.entity.impl.RestaurantFoodEntity
import com.daon.goj_gam.data.preference.AppPreferenceManager
import com.daon.goj_gam.data.repository.food.DefaultRestaurantFoodRepository
import com.daon.goj_gam.data.repository.food.RestaurantFoodRepository
import com.daon.goj_gam.data.repository.map.DefaultMapRepository
import com.daon.goj_gam.data.repository.map.MapRepository
import com.daon.goj_gam.data.repository.order.DefaultOrderRepository
import com.daon.goj_gam.data.repository.order.OrderRepository
import com.daon.goj_gam.data.repository.photo.DefaultGalleryRepository
import com.daon.goj_gam.data.repository.photo.GalleryRepository
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
import com.daon.goj_gam.screen.main.like.RestaurantLikedListViewModel
import com.daon.goj_gam.screen.main.my.MyViewModel
import com.daon.goj_gam.screen.main.review.AddReviewViewModel
import com.daon.goj_gam.screen.main.review.gallery.GalleryViewModel
import com.daon.goj_gam.screen.mylocation.MyLocationViewModel
import com.daon.goj_gam.screen.order.OrderMenuListViewModel
import com.daon.goj_gam.util.event.MenuChangeEventBus
import com.daon.goj_gam.util.provider.DefaultResourcesProvider
import com.daon.goj_gam.util.provider.ResourcesProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single<ResourcesProvider> { DefaultResourcesProvider(androidContext()) }

    // Firebase
    single { Firebase.firestore }
    single { FirebaseStorage.getInstance() }
    single { FirebaseAuth.getInstance() }

    // dispatcher
    single { Dispatchers.IO }
    single { Dispatchers.Main }

    // repository
    single<RestaurantRepository> { DefaultRestaurantRepository(get(), get(), get()) }
    single<MapRepository> { DefaultMapRepository(get(),get()) }
    single<UserRepository> { DefaultUserRepository(get(), get(),get()) }
    single<RestaurantFoodRepository> { DefaultRestaurantFoodRepository(get(), get(), get()) }
    single<RestaurantReviewRepository> { DefaultRestaurantReviewRepository(get(), get(), get()) }
    single<OrderRepository> { DefaultOrderRepository(get(), get()) }
    single<GalleryRepository> { DefaultGalleryRepository(androidContext(), get()) }

    // preference
    single { AppPreferenceManager(androidContext()) }

    // api
    single { providerGsonConvertFactory() }
    single { buildOkHttpClient() }
    single(named("map")) { provideMapRetrofit(get(),get())}
    single(named("food")) { provideFoodRetrofit(get(),get()) }
    single { provideMapApiService(get(qualifier = named("map"))) }
    single { provideFoodApiService(get(qualifier = named("food"))) }

    // dao
    single { provideDB(androidContext()) }
    single { provideLocationDao(get()) }
    single { provideRestaurantDao(get()) }
    single { provideFoodMenuBasketDao(get()) }

    // viewModel
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { MyViewModel(get(), get(), get()) }
    viewModel { (restaurantCategory: RestaurantCategory, locationLatLng: LocationLatLngEntity) ->
        RestaurantListViewModel(restaurantCategory, locationLatLng, get())
    }
    viewModel { (mapSearchInfoEntity: MapSearchInfoEntity) -> MyLocationViewModel(mapSearchInfoEntity, get(), get())}
    viewModel { (restaurant: RestaurantEntity) -> RestaurantDetailViewModel(restaurant, get(), get())}
    viewModel { (restaurantId: Long, foodList: List<RestaurantFoodEntity>) ->
        RestaurantMenuListViewModel(restaurantId, foodList, get())
    }
    viewModel { (restaurantTitle: String) -> RestaurantReviewListViewModel(restaurantTitle, get()) }
    viewModel { RestaurantLikedListViewModel(get()) }
    viewModel { OrderMenuListViewModel(get(), get()) }
    viewModel { AddReviewViewModel(get()) }
    viewModel { GalleryViewModel(get()) }

    // event bus
    single { MenuChangeEventBus() }
}
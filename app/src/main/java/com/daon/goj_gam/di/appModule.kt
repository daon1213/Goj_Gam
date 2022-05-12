package com.daon.goj_gam.di


import com.daon.goj_gam.screen.main.home.HomeViewModel
import com.daon.goj_gam.screen.main.my.MyViewModel
import com.daon.goj_gam.util.provider.DefaultResourcesProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { HomeViewModel() }
    viewModel { MyViewModel() }

    single { providerGsonConvertFactory() }
    single { buildOkHttpClient() }

    single { provideRetrofit(get(), get()) }

    single<com.daon.goj_gam.util.provider.ResourcesProvider> {DefaultResourcesProvider(androidApplication())}

    single { Dispatchers.IO }
    single { Dispatchers.Main }

}
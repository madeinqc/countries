package com.masauve.countrylist.di

import com.masauve.countrylist.api.CountryAPI
import com.masauve.countrylist.api.CountryRepository
import com.masauve.countrylist.mvi.CountryListViewModel
import com.masauve.networking.Networking
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val countryListModule = module {

    // TODO: MAS - Move these 3 to their own feature module
    single {
        Networking.createRetrofit("https://restcountries.eu/rest/v2/", false)
    }

    single {
        get<Retrofit>().create(CountryAPI::class.java)
    }

    single { CountryRepository(get()) }

    viewModel { CountryListViewModel(get(), get()) }
}
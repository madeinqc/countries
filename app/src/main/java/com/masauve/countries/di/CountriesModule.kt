package com.masauve.countries.di

import com.masauve.countries.CountryAPI
import com.masauve.countries.CountryRepository
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {

    factory<CallAdapter.Factory> { RxJava2CallAdapterFactory.create() }

    factory<Converter.Factory> { MoshiConverterFactory.create() }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://restcountries.eu/rest/v2/")
            .addCallAdapterFactory(get())
            .addConverterFactory(get())
            .build()
    }

    single {
        get<Retrofit>().create(CountryAPI::class.java)
    }

    single { CountryRepository(get()) }
}
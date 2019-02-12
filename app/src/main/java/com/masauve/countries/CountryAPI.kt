package com.masauve.countries

import com.squareup.moshi.JsonClass
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

@JsonClass(generateAdapter = true)
data class Country(val name: String)

interface CountryAPI {
    @GET("all")
    fun listCountries(): Single<Response<List<Country>>>
}

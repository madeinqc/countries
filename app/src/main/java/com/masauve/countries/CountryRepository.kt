package com.masauve.countries

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Observable

// TODO: Cache the values to a LRU memory cache
// TODO: Cache the values to Database (room?)
// TODO: Balance where to take the data based on freshness
class CountryRepository(private val countryApi: CountryAPI) {
    fun listCountries(): Observable<Lce<List<Country>>> {
        return countryApi.listCountries()
            .doOnError { TODO("Include Timber library and log") }
            .map { response ->
                response.body()?.let { Lce.Content(it) }
                    ?: response.errorBody()?.let { body ->
                        val moshi: Moshi = Moshi.Builder().build()
                        val type = Types.newParameterizedType(List::class.java, Country::class.java)
                        val adapter: JsonAdapter<List<Country>> = moshi.adapter(type)
                        val data = adapter.fromJson(body.string())
                        Lce.Error(data, null)
                    }
                    ?: Lce.Error(listOf(), null)
            }
            .toObservable()
            .startWith(Lce.Loading())
    }
}

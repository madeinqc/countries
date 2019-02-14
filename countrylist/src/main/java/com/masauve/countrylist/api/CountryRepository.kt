package com.masauve.countrylist.api

import com.masauve.networking.Lce
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Observable

// TODO: Cache the values to a LRU memory cache
// TODO: Cache the values to Database (room?)
// TODO: Balance where to take the data based on freshness
class CountryRepository(private val countryApi: com.masauve.countrylist.api.CountryAPI) {
    // TODO: Implement the forceReload once the cache is done
    fun listCountries(@Suppress("UNUSED_PARAMETER") forceReload: Boolean): Observable<Lce<List<Country>>> {
        return countryApi.listCountries()
            .doOnError { TODO("Include Timber library and log") }
            .map { response ->
                response.body()?.let { Lce.Content(it) }
                    ?: response.errorBody()?.let { body ->
                        val moshi: Moshi = Moshi.Builder().build()
                        val type = Types.newParameterizedType(List::class.java, com.masauve.countrylist.api.Country::class.java)
                        val adapter: JsonAdapter<List<com.masauve.countrylist.api.Country>> = moshi.adapter(type)
                        val data = adapter.fromJson(body.string())
                        Lce.Error(data, null)
                    }
                    ?: Lce.Error(listOf(), null)
            }
            .onErrorReturn { Lce.Error(listOf(), it) }
            .toObservable()
            .startWith(Lce.Loading())
    }
}

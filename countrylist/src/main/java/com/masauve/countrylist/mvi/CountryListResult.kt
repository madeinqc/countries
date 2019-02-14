package com.masauve.countrylist.mvi

import com.masauve.countrylist.api.Country
import com.masauve.mvi.MviResult
import com.masauve.networking.Lce

sealed class CountryListResult : MviResult {
    data class LoadCountriesResult(val countries: Lce<List<Country>>) : CountryListResult()
}
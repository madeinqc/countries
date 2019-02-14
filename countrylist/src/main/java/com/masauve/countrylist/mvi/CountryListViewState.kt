package com.masauve.countrylist.mvi

import com.masauve.countrylist.api.Country
import com.masauve.mvi.MviViewState

data class CountryListViewState(
    val adapterList: List<Country> = emptyList(),
    val isLoading: Boolean
) : MviViewState

package com.masauve.countrylist.mvi

import com.masauve.countrylist.api.Country

data class CountryListViewState(
    val adapterList: List<Country> = emptyList(),
    val isLoading: Boolean
)

sealed class CountryListEvent {
    object LoadingEvent : CountryListEvent()
    object RetryLoadingEvent : CountryListEvent()
    object ViewDetailEvent: CountryListEvent()
}

sealed class CountryListResult {
}

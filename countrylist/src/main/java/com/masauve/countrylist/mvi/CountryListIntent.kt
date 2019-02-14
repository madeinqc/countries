package com.masauve.countrylist.mvi

import com.masauve.mvi.MviIntent

sealed class CountryListIntent : MviIntent {
    object InitialIntent : CountryListIntent()
    object SwipeToRefresh : CountryListIntent()
}
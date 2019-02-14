package com.masauve.countrylist.mvi

import com.masauve.mvi.MviViewEffect

sealed class CountryListViewEffect : MviViewEffect {
    object CountryPressedToastEffect : CountryListViewEffect()
    object LoadingToastEffect : CountryListViewEffect()
}

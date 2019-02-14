package com.masauve.countrylist.mvi

import com.masauve.mvi.MviAction

sealed class CountryListAction : MviAction {
    data class LoadCountriesAction(val forceReload: Boolean) : CountryListAction()
}

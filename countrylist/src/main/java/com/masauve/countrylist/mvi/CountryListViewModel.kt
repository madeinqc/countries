package com.masauve.countrylist.mvi

import android.app.Application
import com.masauve.androidmvi.BaseMviViewModel
import com.masauve.countrylist.api.CountryRepository
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

class CountryListViewModel(app: Application, countryListRepository: CountryRepository) : BaseMviViewModel<
        CountryListIntent,
        CountryListAction,
        CountryListResult,
        CountryListViewState,
        CountryListViewEffect>(
    app,
    initialViewState = CountryListViewState(isLoading = true),
    stateReducer = stateReducer,
    viewEffectTransformer = viewEffectTransformer,
    interactor = CountryListInteractor(countryListRepository)
) {
    override val intentsErrorHandler: (t: Throwable) -> Unit = {
        TODO("Log with Timber")
    }

    override fun actionFromIntent(intent: CountryListIntent): CountryListAction {
        return when (intent) {
            CountryListIntent.InitialIntent -> CountryListAction.LoadCountriesAction(false)
            CountryListIntent.SwipeToRefresh -> CountryListAction.LoadCountriesAction(false)
        }
    }

}

private val stateReducer = BiFunction { viewState: CountryListViewState, result: CountryListResult ->
    return@BiFunction when (result) {
        is CountryListResult.LoadCountriesResult -> viewState.copy(
            adapterList = result.countries.valueOrNull() ?: emptyList(),
            isLoading = result.countries.isLoading()
        )
    }
}

private val viewEffectTransformer = ObservableTransformer<CountryListResult, CountryListViewEffect> { upstream ->
    upstream.map {
        return@map when (it) {
            is CountryListResult.LoadCountriesResult -> CountryListViewEffect.LoadingToastEffect
        }
    }
}
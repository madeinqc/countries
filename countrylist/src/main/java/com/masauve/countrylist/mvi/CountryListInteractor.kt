package com.masauve.countrylist.mvi

import com.masauve.countrylist.api.CountryRepository
import com.masauve.countrylist.mvi.CountryListAction.LoadCountriesAction
import com.masauve.countrylist.mvi.CountryListResult.LoadCountriesResult
import com.masauve.mvi.MviInteractor
import io.reactivex.ObservableTransformer

class CountryListInteractor(private val countryListRepository: CountryRepository) :
    MviInteractor<CountryListAction, CountryListResult> {

    override val actionProcessor: ObservableTransformer<CountryListAction, CountryListResult> =
        ObservableTransformer { upstream ->
            upstream.publish { o ->
                // TODO: Use `Observable.merge` once there is more actions and remove the `cast` operation
                o.ofType(LoadCountriesAction::class.java)
                    .compose(onLoadCountries())
                    .cast(CountryListResult::class.java)
            }
        }

    private fun onLoadCountries(): ObservableTransformer<LoadCountriesAction, LoadCountriesResult> =
        ObservableTransformer { upstream ->
            upstream.flatMap { countryListRepository.listCountries(it.forceReload) }.map { LoadCountriesResult(it) }
        }
}

package com.masauve.countrylist

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintSet.PARENT_ID
import android.view.View
import android.view.View.TEXT_ALIGNMENT_CENTER
import com.jakewharton.rxbinding2.view.clicks
import com.masauve.countrylist.api.Country
import com.masauve.countrylist.mvi.CountryListIntent
import com.masauve.countrylist.mvi.CountryListViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.constraintLayout
import org.koin.android.viewmodel.ext.viewModel

class CountryListActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    private val viewModel: CountryListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CountryListUI(viewModel, compositeDisposable).setContentView(this)
    }

}

class CountryListUI(
    private val viewModel: CountryListViewModel,
    private val compositeDisposable: CompositeDisposable
) : AnkoComponent<CountryListActivity> {
    override fun createView(ui: AnkoContext<CountryListActivity>) = with(ui) {
        constraintLayout {

            val loadingIndicator = spinner()
                .lparams(dip(32), dip(32)) {
                    topToTop = PARENT_ID
                    bottomToBottom = PARENT_ID
                    startToStart = PARENT_ID
                    endToEnd = PARENT_ID
                }

            val emptyView = textView("Cannot load countries or there is no countries") {
                textAlignment = TEXT_ALIGNMENT_CENTER
            }
                .lparams(matchParent, wrapContent) {
                    topToTop = PARENT_ID
                    bottomToBottom = PARENT_ID
                    startToStart = PARENT_ID
                    endToEnd = PARENT_ID
                }

            val countryList = textView {
                backgroundColor = Color.LTGRAY
            }.lparams(matchParent, matchParent)

            viewModel.viewStateObservable
                .subscribe {
                    loadingIndicator.visibility = if (it.isLoading) View.VISIBLE else View.GONE

                    emptyView.visibility = if (!it.isLoading && it.adapterList.isEmpty()) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }

                    countryList.visibility = if (!it.isLoading && it.adapterList.isNotEmpty()) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }

                    countryList.text = it.adapterList.joinToString(", ", transform = Country::name)
                }
                .addTo(compositeDisposable)

            viewModel
                .processIntents(
                    Observable.just(CountryListIntent.InitialIntent),
                    emptyView.clicks().map { CountryListIntent.SwipeToRefresh },
                    countryList.clicks().map { CountryListIntent.SwipeToRefresh }
                )
                .addTo(compositeDisposable)
        }
    }
}
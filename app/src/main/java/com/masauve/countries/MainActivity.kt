package com.masauve.countries

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.masauve.countrylist.mvi.CountryListIntent
import com.masauve.countrylist.mvi.CountryListViewEffect
import com.masauve.countrylist.mvi.CountryListViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import org.koin.android.viewmodel.ext.viewModel

class MainActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    // TODO: Move this to a ViewModel
    private val countryListViewModel: CountryListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countryListViewModel.viewEffectObservable
            .subscribe {
                when (it) {
                    is CountryListViewEffect.LoadingToastEffect ->
                        Toast.makeText(this, "Loading countries", Toast.LENGTH_SHORT).show()
                }
            }
            .addTo(compositeDisposable)

        // TODO: Make a list out of this
        countryListViewModel.viewStateObservable
            .subscribe {
                it.adapterList.takeIf { it.isNotEmpty() }?.let {
                    Log.d(this.javaClass.name, it.toString())
                }
            }
            .addTo(compositeDisposable)

        val testSubject = PublishSubject.create<CountryListIntent>()
        countryListViewModel.processIntents(testSubject.hide())
            .addTo(compositeDisposable)

        testSubject.onNext(CountryListIntent.InitialIntent)

        Handler().runAfter(5000) {
            testSubject.onNext(CountryListIntent.SwipeToRefresh)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
    }

}

private fun Handler.runAfter(delayMillis: Long, f: () -> Unit) {
    postDelayed(f, delayMillis)
}

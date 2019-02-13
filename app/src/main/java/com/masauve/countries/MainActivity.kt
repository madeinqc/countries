package com.masauve.countries

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.masauve.countrylist.api.CountryRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    // TODO: Move this to a ViewModel
    private val countryRepository: CountryRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO: Make a list out of this
        countryRepository.listCountries()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { Log.d(this.javaClass.name, it.toString()) }
            .addTo(compositeDisposable)
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
    }

}

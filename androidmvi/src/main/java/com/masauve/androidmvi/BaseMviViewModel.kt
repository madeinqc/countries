package com.masauve.androidmvi

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.masauve.mvi.*
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.PublishSubject.create

abstract class BaseMviViewModel<Intent : MviIntent, Action : MviAction, Result : MviResult, ViewState : MviViewState, ViewEffect : MviViewEffect>(
    app: Application,
    stateReducer: BiFunction<ViewState, Result, ViewState>,
    viewEffectTransformer: ObservableTransformer<Result, ViewEffect>,
    interactor: MviInteractor<Action, Result>
) : AndroidViewModel(app), MviViewModel<Intent, ViewState, ViewEffect> {

    protected var viewModelDisposable: Disposable? = null
        private set

    protected abstract val initialViewState: ViewState

    abstract val intentsErrorHandler: ((t: Throwable) -> Unit)

    /**
     *  Proxy subject used to keep the stream alive even after the UI gets recycled.
     *  This is basically used to keep ongoing events and the last cached State alive
     *  while the UI disconnects and reconnects on config changes.
     */
    protected val intentSubject: PublishSubject<Intent> = create()

    protected val viewStateSubject: BehaviorSubject<ViewState> = BehaviorSubject.create()

    protected val viewEffectSubject: PublishSubject<ViewEffect> = PublishSubject.create()

    /**
     *  Transform a given [MviIntent] to the appropriate [MviAction].
     */
    protected abstract fun actionFromIntent(intent: Intent): Action

    init {
        val viewChanges = intentSubject
            .map(::actionFromIntent)
            .compose(interactor.actionProcessor)
            .publish()

        viewChanges
            .compose(viewStateTransformer(stateReducer))
            .subscribe(viewStateSubject)

        viewChanges
            .compose(viewEffectTransformer)
            .subscribe(viewEffectSubject)

        viewChanges.autoConnect(0) { viewModelDisposable = it }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelDisposable?.dispose()
    }

    fun processInputs(vararg intents: Observable<out Intent>): Disposable {
        return Observable.mergeArray(*intents)
            .subscribe(
                { intentSubject.onNext(it) },
                intentsErrorHandler
            )
    }

    fun viewStateObservable(): Observable<ViewState> = viewStateSubject.hide()

    fun viewEffectObservable(): Observable<ViewEffect> = viewEffectSubject.hide()

    private fun viewStateTransformer(
        stateReducer: BiFunction<ViewState, Result, ViewState>
    ): ObservableTransformer<Result, ViewState> {
        return ObservableTransformer { upstream ->
            upstream.scan(viewStateSubject.value ?: initialViewState, stateReducer)
                .distinctUntilChanged()
        }
    }
}
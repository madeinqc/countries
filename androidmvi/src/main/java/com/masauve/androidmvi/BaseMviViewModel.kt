package com.masauve.androidmvi

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.masauve.mvi.*
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.PublishSubject.create

abstract class BaseMviViewModel<Intent : MviIntent, Action : MviAction, Result : MviResult, ViewState : MviViewState, ViewEffect : MviViewEffect>(
    app: Application,
    initialViewState: ViewState,
    stateReducer: BiFunction<ViewState, Result, ViewState>,
    viewEffectTransformer: ObservableTransformer<Result, ViewEffect>,
    interactor: MviInteractor<Action, Result>
) : AndroidViewModel(app), MviViewModel<Intent, ViewState, ViewEffect> {

    protected var viewModelDisposable: Disposable? = null
        private set

    protected abstract val intentsErrorHandler: ((t: Throwable) -> Unit)

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
            .observeOn(Schedulers.computation())
            .map(::actionFromIntent)
            .compose(interactor.actionProcessor)
            .publish()

        viewChanges
            .compose(viewStateTransformer(initialViewState, stateReducer))
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

    override fun processIntents(vararg intents: Observable<out Intent>): Disposable {
        return Observable.mergeArray(*intents)
            .subscribe(
                { intentSubject.onNext(it) },
                intentsErrorHandler
            )
    }

    override val viewStateObservable: Observable<ViewState> = viewStateSubject.hide()
        .observeOn(AndroidSchedulers.mainThread())

    override val viewEffectObservable: Observable<ViewEffect> = viewEffectSubject.hide()
        .observeOn(AndroidSchedulers.mainThread())

    private fun viewStateTransformer(
        initialViewState: ViewState,
        stateReducer: BiFunction<ViewState, Result, ViewState>
    ): ObservableTransformer<Result, ViewState> {
        return ObservableTransformer { upstream ->
            upstream.scan(viewStateSubject.value ?: initialViewState, stateReducer)
                .distinctUntilChanged()
        }
    }
}
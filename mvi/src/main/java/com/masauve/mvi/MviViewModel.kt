package com.masauve.mvi

import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 *  Subscribes to a [MviView]'s [MviIntent] to process them and return a [MviViewState].
 */
interface MviViewModel<Intent : MviIntent, ViewState : MviViewState, ViewEffect : MviViewEffect> {

    val viewStateObservable: Observable<ViewState>

    val viewEffectObservable: Observable<ViewEffect>

    fun processIntents(vararg intents: Observable<out Intent>): Disposable
}
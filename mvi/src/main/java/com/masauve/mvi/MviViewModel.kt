package com.masauve.mvi

import io.reactivex.Observable

/**
 *  Subscribes to a [MviView]'s [MviIntent] to process them and return a [MviViewState].
 */
interface MviViewModel<Intent : MviIntent, ViewState : MviViewState, ViewEffect : MviViewEffect> {

    val stateObservable: Observable<ViewState>

    val viewEffectObservable: Observable<ViewEffect>

    fun processIntents(intent: Observable<Intent>)
}
package com.masauve.mvi

import io.reactivex.Observable

/**
 *  Represent a UI that can emit [MviIntent] to a [MviViewModel] and will render the [MviViewState] provided in return.
 */
interface MviView<Intent : MviIntent, ViewState : MviViewState> {

    /**
     *  Used by the [MviViewModel] to obtain the [MviIntent]. All the [MviView]'s [MviIntent]s must go through this
     *  [Observable].
     */
    fun intents(): Observable<Intent>

    /**
     *  Used to render the [MviViewState] to the screen. The rendering should be made so that there is no side effects
     *  left when rendering a given state. For example, if a view should be shown or hidden, the render method should
     *  make sure that it is always set according to the [MviViewState].
     */
    fun render(state: ViewState)
}
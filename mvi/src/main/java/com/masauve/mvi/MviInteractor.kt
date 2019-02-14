package com.masauve.mvi

import io.reactivex.Observable
import io.reactivex.ObservableTransformer

/**
 *  Defines the required business logic to transform a [MviAction] to a [MviResult].
 */
interface MviInteractor<Action : MviAction, Result : MviResult> {

    /**
     * Splits the [Observable] to match each type of [MviAction] to
     * its corresponding business logic. Each takes a defined [MviAction],
     * returns a defined [MviResult]
     * The global actionProcessor then merges all [Observable] back to
     * one unique [Observable].
     *
     *
     * The splitting is done using [Observable.publish] which allows almost anything
     * on the passed [Observable] as long as one and only one [Observable] is returned.
     *
     *
     * An security layer is also added for unhandled [MviAction] to allow early crash
     * at runtime to easy the maintenance.
     */
    val actionProcessor: ObservableTransformer<Action, Result>
}
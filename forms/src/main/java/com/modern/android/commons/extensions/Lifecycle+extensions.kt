package com.modern.commons.android.extensions

import androidx.lifecycle.GenericLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver

/**
 * Attaches an observer to [Lifecycle] and invokes [block] every time that specified [event] occurs.
 */
inline fun Lifecycle.addEventObserver(event: Lifecycle.Event, crossinline block: () -> Unit): LifecycleObserver =
    GenericLifecycleObserver { _, currentEvent ->
        if (currentEvent == event) {
            block()
        }
    }.also {
        addObserver(it)
    }

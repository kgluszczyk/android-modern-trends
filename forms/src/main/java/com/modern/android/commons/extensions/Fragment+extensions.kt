package com.modern.commons.android.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.GenericLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.ViewModelProviders
import kotlin.properties.Delegates

fun <T : ViewModel> Fragment.create(factory: Factory, clazz: Class<T>): T {
    return ViewModelProviders.of(this, factory).get(clazz)
}

/**
 * Subscribes to [Fragment.onDestroyView] and performs operation given in [block].
 * Remember that this is one time event that has to be registered after [Fragment.onCreateView].
 */
inline fun Fragment.doOnDestroyView(crossinline block: () -> Unit) {
    viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroyView() {
            viewLifecycleOwner.lifecycle.removeObserver(this)
            block()
        }
    })
}

/**
 * Registers for [Fragment.getViewLifecycleOwnerLiveData] updates and accordingly removes or adds observer
 * that invokes [block] when specified [event] occurs.
 *
 * All of this logic is necessary, because [Fragment.getViewLifecycleOwner] is available only when the view is created,
 * (and not when the fragment is created).
 */
inline fun Fragment.addViewLifecycleObserver(event: Lifecycle.Event, crossinline block: () -> Unit) {
    val observer = GenericLifecycleObserver { _, lifecycleEvent ->
        if (event == lifecycleEvent) {
            block()
        }
    }

    @Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
    var lifecycleOwner by Delegates.observable<LifecycleOwner?>(null) { _, oldValue, newValue ->
        oldValue?.lifecycle?.removeObserver(observer)
        newValue?.lifecycle?.addObserver(observer)
    }

    viewLifecycleOwnerLiveData
        .observe(this, Observer { lifecycleOwner = it })

}

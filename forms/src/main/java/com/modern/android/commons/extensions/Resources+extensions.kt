package com.modern.commons.android.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.annotation.Px
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.lazyDimensionRes(@DimenRes resId: Int): Lazy<Int> =
        lazyContext { dimensionRes(resId) }

fun Fragment.lazyIntegerRes(@IntegerRes resId: Int): Lazy<Int> =
        lazyContext { integerRes(resId) }

fun Fragment.lazyLongRes(@IntegerRes resId: Int): Lazy<Long> =
        lazyContext { longRes(resId) }

fun Fragment.lazyDrawable(@DrawableRes resId: Int): Lazy<Drawable> =
        lazyContext { drawableRes(resId) }

fun Fragment.lazyColorRes(@ColorRes resId: Int): Lazy<Int> =
        lazyContext { colorRes(resId) }

fun Fragment.lazyStringRes(@StringRes resId: Int): Lazy<String> =
        lazyContext { stringRes(resId) }

private inline fun <T> Fragment.lazyContext(crossinline lambda: Context.() -> T): Lazy<T> =
        lazy { lambda(requireContext()) }

fun Context.lazyDimensionRes(@DimenRes resId: Int): Lazy<Int> =
        lazy { dimensionRes(resId) }

fun Context.lazyIntegerRes(@IntegerRes resId: Int): Lazy<Int> =
        lazy { integerRes(resId) }

fun Context.lazyLongRes(@IntegerRes resId: Int): Lazy<Long> =
        lazy { integerRes(resId).toLong() }

fun Context.lazyDrawable(@DrawableRes resId: Int): Lazy<Drawable> =
        lazy { drawableRes(resId) }

fun Context.lazyColorRes(@ColorRes resId: Int): Lazy<Int> =
        lazy { colorRes(resId) }

fun Context.lazyStringRes(@StringRes resId: Int): Lazy<String> =
        lazy { stringRes(resId) }

@Px
fun Context.dimensionRes(@DimenRes resId: Int): Int =
        resources.getDimensionPixelSize(resId)

fun Context.integerRes(@IntegerRes resId: Int): Int =
        resources.getInteger(resId)

fun Context.longRes(@IntegerRes resId: Int): Long =
        resources.getInteger(resId).toLong()

fun Context.drawableRes(@DrawableRes resId: Int): Drawable =
        requireNotNull(ContextCompat.getDrawable(this, resId))

fun Context.colorRes(@ColorRes resId: Int): Int =
        requireNotNull(ContextCompat.getColor(this, resId))

fun Context.stringRes(@StringRes resId: Int): String =
        resources.getString(resId)

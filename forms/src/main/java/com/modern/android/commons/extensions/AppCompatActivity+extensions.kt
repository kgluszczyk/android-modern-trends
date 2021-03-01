@file:JvmName("AppCompatActivityUtil")

package com.modern.commons.android.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.ViewModelProviders

fun <T : ViewModel> AppCompatActivity.create(factory: Factory, clazz: Class<T>): T {
    return ViewModelProviders.of(this, factory).get(clazz)
}
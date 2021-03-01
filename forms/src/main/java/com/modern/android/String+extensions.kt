package com.modern.android.forms

fun String.fromApiBoolean() = toBoolean() || this.equals("Yes", ignoreCase = true)

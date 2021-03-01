package com.modern.android.forms

interface SessionAwareActivity {
    fun sessionExpired(error: Throwable)
}
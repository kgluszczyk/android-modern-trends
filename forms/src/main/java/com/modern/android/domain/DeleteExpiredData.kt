package com.modern.android.forms.domain

import androidx.annotation.WorkerThread

interface DeleteExpiredData {

    @WorkerThread
    fun execute()
}
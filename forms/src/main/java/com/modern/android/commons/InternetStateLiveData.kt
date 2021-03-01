package com.modern.commons.network

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData

class InternetStateLiveData(val context: Context) : LiveData<Boolean>() {

    private var receiver: BroadcastReceiver? = null
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @SuppressLint("MissingPermission")
    fun hasInternetConnection() = connectivityManager.activeNetworkInfo?.isConnectedOrConnecting ?: false

    override fun onActive() {
        updateValue()
        registerReceiver()
    }

    private fun updateValue() {
        val hasInternetConnection = hasInternetConnection()
        if (hasInternetConnection != value) {
            value = hasInternetConnection
        }
    }

    override fun onInactive() {
        unregisterReceiver()
    }

    private fun registerReceiver() {
        if (receiver == null) {
            val filter = IntentFilter().apply {
                addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            }
            receiver = NetworkStateChangeReceiver()
            context.registerReceiver(receiver, filter)
        }
    }

    private fun unregisterReceiver() {
        if (receiver != null) {
            context.unregisterReceiver(receiver)
            receiver = null
        }
    }

    inner class NetworkStateChangeReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            updateValue()
        }
    }
}
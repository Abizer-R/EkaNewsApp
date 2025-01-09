package com.abizer_r.data.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkConnectionObserver @Inject constructor(
    private val context: Context
) {

    private val _isNetworkAvailable = MutableStateFlow(false)
    val isNetworkAvailable: StateFlow<Boolean> get() = _isNetworkAvailable

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private var isConnectivityCallbackRegistered = false

    private val request: NetworkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
        .build()

    val connectivityCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _isNetworkAvailable.update { true }
        }

        override fun onUnavailable() {
            _isNetworkAvailable.update { false }
        }

        override fun onLost(network: Network) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            if (networkCapabilities == null) {
                _isNetworkAvailable.update { false }
            } else {
                handleNetworkCapabilitiesChanged(networkCapabilities)
            }
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            handleNetworkCapabilitiesChanged(networkCapabilities)
        }

    }

    init {
        connectivityManager.registerNetworkCallback(request, connectivityCallback)
        isConnectivityCallbackRegistered = true

        ProcessLifecycleOwner.get().lifecycle
            .addObserver(object : DefaultLifecycleObserver {
                override fun onResume(owner: LifecycleOwner) {
                    super.onResume(owner)
                    if (isConnectivityCallbackRegistered.not()) {
                        connectivityManager.registerNetworkCallback(request, connectivityCallback)
                        isConnectivityCallbackRegistered = true
                    }
                }

                override fun onStop(owner: LifecycleOwner) {
                    super.onStop(owner)
                    if (isConnectivityCallbackRegistered) {
                        connectivityManager.unregisterNetworkCallback(connectivityCallback)
                        isConnectivityCallbackRegistered = false
                    }
                }
            })
    }

    private fun handleNetworkCapabilitiesChanged(networkCapabilities: NetworkCapabilities) {
        val isInternet =
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

        val isValidated =
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

        _isNetworkAvailable.update { isInternet && isValidated }
    }

    fun isConnected(): Boolean {
        return _isNetworkAvailable.value
    }
}
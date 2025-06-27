package com.example.myapplication.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.widget.Toast

class BrodcastRec : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        try {
            if (isOnline(context)) {
                Toast.makeText(context, "Network Connected", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "No Network Connected", Toast.LENGTH_SHORT).show()
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }

}
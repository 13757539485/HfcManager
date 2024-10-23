package com.hfc.manager

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent

class HfcBootReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                intent.component = ComponentName(context, HfcService::class.java)
                context.startService(intent)
            }
        }
    }
}
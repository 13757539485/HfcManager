package com.hfc.manager

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent

class HfcManagerReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        intent.component = ComponentName(context, HfcService::class.java)
        context.startService(intent)
    }
}
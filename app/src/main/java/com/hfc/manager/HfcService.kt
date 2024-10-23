package com.hfc.manager

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import android.util.Log
import android.widget.Toast


class HfcService: Service() {
    private val TAG = "yuli"

    override fun onBind(intent: Intent?) = null

    override fun onUnbind(intent: Intent?): Boolean {
        Log.e(TAG, "onUnbind: ")
        return super.onUnbind(intent)
    }

    private fun collectShareAction(pkg: String, type : String): MutableList<ResolveInfo> {
        val intent = Intent(Intent.ACTION_SEND, null)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.type = type
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.queryIntentActivities(
                intent,
                PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
            )
        } else {
            packageManager.queryIntentActivities(
                intent,
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
            )
        }.filter {
            it.activityInfo.packageName == pkg
        }.toMutableList()
    }

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "onCreate: ")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand: $intent")
        Toast.makeText(this, "onStartCommand: $intent", Toast.LENGTH_SHORT).show()
        intent?.let {
            when (it.action) {
                ACTION_SHOW -> {
                }
                ACTION_HIDE -> {
                }
            }
        }
        return START_STICKY
    }
}
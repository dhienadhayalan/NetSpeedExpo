package com.dhiena.netspeedexpo

import android.content.Intent
import android.os.Build
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class SpeedServiceModule(private val reactContext: ReactApplicationContext)
  : ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String = "SpeedService"

  @ReactMethod
  fun start() {
    val intent = Intent(reactContext, SpeedForegroundService::class.java)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      reactContext.startForegroundService(intent)
    } else {
      reactContext.startService(intent)
    }
  }

  @ReactMethod
  fun stop() {
    val intent = Intent(reactContext, SpeedForegroundService::class.java)
    reactContext.stopService(intent)
  }
}

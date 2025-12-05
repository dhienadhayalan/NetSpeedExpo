package com.dhiena.netspeedexpo

import android.app.Service
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import android.content.pm.ServiceInfo          // <-- Required import
import java.util.*
import kotlin.concurrent.fixedRateTimer
import java.net.HttpURLConnection
import java.net.URL

class SpeedForegroundService : Service() {

  private val CHANNEL_ID = "speed_channel"
  private val NOTIFICATION_ID = 101
  private var timer: Timer? = null

  override fun onCreate() {
    super.onCreate()
    createNotificationChannel()
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

    val notification = buildNotification("Starting...")

    // REQUIRED for Android 10-15 when targetSdk=36
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      startForeground(
        NOTIFICATION_ID,
        notification,
        ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
      )
    } else {
      startForeground(NOTIFICATION_ID, notification)
    }

    // Cancel old timer if exists
    timer?.cancel()

    // Timer that updates every 10 seconds
    timer = fixedRateTimer("speedTimer", false, 0, 10_000) {
      val speed = measureSpeed()
      updateNotification("Speed: $speed")
    }

    return START_STICKY
  }

  override fun onDestroy() {
    timer?.cancel()
    super.onDestroy()
  }

  override fun onBind(intent: Intent?): IBinder? = null

  // Create high-importance notification channel for OPPO/Realme
  private fun createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channel = NotificationChannel(
        CHANNEL_ID,
        "Internet Speed Tracking",
        NotificationManager.IMPORTANCE_HIGH
      )
      channel.description = "Shows speed update every 10 seconds"

      val manager = getSystemService(NotificationManager::class.java)
      manager?.createNotificationChannel(channel)
    }
  }

  private fun buildNotification(text: String): Notification {
    return NotificationCompat.Builder(this, CHANNEL_ID)
      .setContentTitle("Internet Speed Tracking")
      .setContentText(text)
      .setSmallIcon(R.mipmap.ic_launcher)
      .setPriority(NotificationCompat.PRIORITY_MAX)   // OPPO needs HIGH priority
      .setOnlyAlertOnce(true)
      .setOngoing(true)
      .build()
  }

  private fun updateNotification(text: String) {
    val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    manager.notify(NOTIFICATION_ID, buildNotification(text))
  }

  // Measures pseudo-internet speed
  private fun measureSpeed(): String {
    return try {
      val url = URL("https://www.google.com/generate_204")
      val start = System.currentTimeMillis()

      val conn = url.openConnection() as HttpURLConnection
      conn.connectTimeout = 4000
      conn.inputStream.use { }
      conn.disconnect()

      val latency = (System.currentTimeMillis() - start).coerceAtLeast(1)
      val fakeMbps = kotlin.math.min(200.0, (1000.0 / latency) * 8.0)

      String.format("%.2f Mbps", fakeMbps)
    } catch (e: Exception) {
      "No Internet"
    }
  }
}

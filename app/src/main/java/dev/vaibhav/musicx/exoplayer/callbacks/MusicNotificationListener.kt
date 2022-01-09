package dev.vaibhav.musicx.exoplayer.callbacks

import android.app.Notification
import android.content.Intent
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import dev.vaibhav.musicx.exoplayer.MusicService
import dev.vaibhav.musicx.utils.NOTIFICATION_ID

class MusicNotificationListener(private val musicService: MusicService) :
    PlayerNotificationManager.NotificationListener {

    override fun onNotificationPosted(
        notificationId: Int,
        notification: Notification,
        ongoing: Boolean
    ) {
        super.onNotificationPosted(notificationId, notification, ongoing)
        musicService.apply {
            if (ongoing && !isRunning) {
                ContextCompat.startForegroundService(
                    this, Intent(applicationContext, this::class.java)
                )
                startForeground(NOTIFICATION_ID, notification)
                isRunning = true
            }
        }
    }

    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        super.onNotificationCancelled(notificationId, dismissedByUser)
        musicService.apply {
            stopForeground(true)
            isRunning = false
            stopSelf()
        }
    }
}

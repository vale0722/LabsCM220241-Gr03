package com.example.jetcaster.core.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import com.example.jetcaster.core.data.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.UUID


private lateinit var workRequestId: UUID

class AudioPlayerWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private var mediaPlayer: MediaPlayer? = null

    override suspend fun doWork(): Result {
        val audioUrl = inputData.getString("AUDIO_URL") ?: return Result.failure()

        setForeground(createForegroundInfo())

        return withContext(Dispatchers.IO) {
            try {
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(audioUrl)
                    prepare()
                    start()
                }

                while (mediaPlayer?.isPlaying == true) {
                    if (isStopped) {
                        stopMediaPlayer()
                        return@withContext Result.success()
                    }
                    delay(1000)
                }

                stopMediaPlayer()
                Result.success()
            } catch (e: Exception) {
                stopMediaPlayer()
                Result.failure()
            }
        }
    }

    private fun stopMediaPlayer() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
            mediaPlayer = null
        }
    }

    private fun createForegroundInfo(): ForegroundInfo {
        createNotificationChannel()

        val notification = NotificationCompat.Builder(applicationContext, "audio_channel")
            .setContentTitle("Playing Audio")
            .setContentText("Your audio is playing in the background")
            .setSmallIcon(android.R.drawable.ic_lock_silent_mode_off)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        return ForegroundInfo(1, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Audio Playback"
            val descriptionText = "Notification channel for audio playback"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel("audio_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

fun playAudio(context: Context, audioUrl: String) {
    val data = Data.Builder()
        .putString("AUDIO_URL", audioUrl)
        .build()

    val audioWorkRequest: WorkRequest = OneTimeWorkRequest.Builder(AudioPlayerWorker::class.java)
        .setInputData(data)
        .build()

    workRequestId = audioWorkRequest.id

    WorkManager.getInstance(context).enqueue(audioWorkRequest)
}

fun stopAudio(context: Context) {
    WorkManager.getInstance(context).cancelWorkById(workRequestId)
}
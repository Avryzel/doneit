package id.jel.doneit.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.core.app.NotificationCompat
import android.app.NotificationChannel
import android.app.NotificationManager
import id.jel.doneit.data.local.AppDatabase
import kotlinx.coroutines.runBlocking

class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val taskId = inputData.getInt("TASK_ID", -1)
        val taskTitle = inputData.getString("TASK_TITLE") ?: "Task"

        val database = AppDatabase.getDatabase(applicationContext)

        val isStillPending = runBlocking {
            val task = database.taskDao().getTaskById(taskId)
            task != null && !task.status
        }

        if (isStillPending) {
            sendNotification(taskTitle)
        }

        return Result.success()
    }

    private fun sendNotification(title: String) {
        val channelId = "deadline_channel"
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(channelId, "Deadlines", NotificationManager.IMPORTANCE_HIGH)
        manager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Deadline Expired!")
            .setContentText("Status: Belum Selesai - $title")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}


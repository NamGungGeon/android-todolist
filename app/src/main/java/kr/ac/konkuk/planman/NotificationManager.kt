package kr.ac.konkuk.planman

import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlin.random.Random

class NotificationManager {
    companion object {
        private var inst: NotificationManager? = null
        fun getInstance(): NotificationManager {
            if (inst == null)
                inst = NotificationManager()
            return inst!!
        }
    }

    private val channelId = "TodoNotification"
    private val channelName = "tn"

    fun sendNotification(context: Context, todo: MyData2):Int {
        val nChannel =
            NotificationChannel(channelId, channelName, android.app.NotificationManager.IMPORTANCE_HIGH)
        nChannel.enableVibration(true)
        nChannel.enableLights(true)
        nChannel.lightColor = Color.BLUE
        nChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        //build notification
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_baseline_checklist_rtl_24)
            .setContentTitle("일정 알림")
            .setContentText(todo.title)
            .setAutoCancel(true)

        val intent = Intent(context, CheckTodoActivity::class.java)
//        intent.putExtra("data", todo)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        val pIntent = PendingIntent.getActivity(
            context,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.setContentIntent(pIntent)

        val notification = builder.build()


        //register notification channel/notification to notification service
        val manager =
            context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as android.app.NotificationManager
        manager.createNotificationChannel(nChannel)

        val notificationId = Random.nextInt()+1
        manager.notify(notificationId, notification)
        return notificationId
    }
}
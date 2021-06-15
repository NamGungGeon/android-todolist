package kr.ac.konkuk.planman

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import androidx.core.app.NotificationCompat

class TimeAlarmReceiver : BroadcastReceiver() {
    lateinit var mediaPlayer : MediaPlayer
    val id = "TimeChannel"
    val name = "TimeCheckChannel"
    lateinit var data : MyData

    //기기가 다시 시작되면 알람 시작
    override fun onReceive(context: Context?, intent: Intent?) {
        val getBundle = intent!!.getParcelableExtra<Bundle>("timeDataByBundle")
        data = getBundle!!.getSerializable("bundleData") as MyData


        //NotificationChannel, Builder 설정
        val notificationChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.enableVibration(true)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC   //content 까지 보여줌

        val builder = NotificationCompat.Builder(context!!, id)
            .setSmallIcon(R.drawable.ic_baseline_alarm_24)
            .setContentTitle(data.title)
//            .setContentText("${data.notifyDateTime!!.month}월 ${data.notifyDateTime!!.dayOfMonth}일 ${data.notifyDateTime!!.hour} 시 ${data.notifyDateTime!!.minute} 분 : [${data.type}]-${data.content}")
            .setAutoCancel(true)


        //Notify 창(헤더 창) 클릭 이벤트
        val toMainIntent = Intent(context, MainActivity::class.java)
        toMainIntent.putExtra("timeNotification", data.title)
        toMainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        val toMainPendingIntent = PendingIntent.getActivity(context, 1, toMainIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(toMainPendingIntent)

        //notification 매니저 설정
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(notificationChannel)

        val notification = builder.build()
        manager.notify(10, notification)

//        case 1
//         */
//        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val ringtone = RingtoneManager.getRingtone(context, notification)
//
//        ringtone.play()

        /*
        case 2 : 오류있음
         */
//        var alert = RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_ALARM)
//        mediaPlayer = MediaPlayer()
//        mediaPlayer.setDataSource(context!!, alert)
//
//        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager




//        if (intent!!.action == "android.intent.action.BOOT_COMPLETED") {
//
//        }

//        mediaPlayer = MediaPlayer.create(this, R.raw.alarm)
//        mediaPlayer.start()

    }




}
package kr.ac.konkuk.planman

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import java.util.*

class TimeAlarmManager {
    var calendar : Calendar = Calendar.getInstance()
    var calendar2 : Calendar = Calendar.getInstance()

    lateinit var now : Date
    val id = "TimeChannel"
    val name = "TimeCheckChannel"

    lateinit var timeData : MyData

    //현재 시간 가져오기(미완성)
    private fun getNowTime() : Calendar {
        val nowByLong = System.currentTimeMillis()
        now = Date(nowByLong)
        val nowByCalendar = Calendar.getInstance()
        
        //시간 설정할것
        return nowByCalendar
    }

    fun reservationTimeAlarm(data : MyData, context: Context) {
        timeData = MyData(data.title, data.content, data.type, data.webSite, data.phoneNumber, data.location, data.notifyDateTime, data.notifyRadius)

        Log.e("reservationAlarm()", "test")
        //data 에서 가져온 시간 설정
        calendar.set(Calendar.YEAR, data.notifyDateTime!!.year)
        calendar.set(Calendar.MONTH, data.notifyDateTime!!.monthValue)
        calendar.set(Calendar.DAY_OF_MONTH, data.notifyDateTime!!.dayOfMonth)
        calendar.set(Calendar.HOUR_OF_DAY, data.notifyDateTime!!.hour)
        calendar.set(Calendar.MINUTE, data.notifyDateTime!!.minute)
        calendar.set(Calendar.SECOND, 0)

        Log.e("year", "${data.notifyDateTime!!.year}")
        Log.e("MONTH", " ${data.notifyDateTime!!.monthValue} ")
        Log.e("DAY_OF_MONTH", "${data.notifyDateTime!!.dayOfMonth}")
        Log.e("HOUR_OF_DAY", "${data.notifyDateTime!!.hour}")
        Log.e("MINUTE", "${data.notifyDateTime!!.minute}")

        //현재일보다 이전이면 등록 실패
//        if (calendar.before(Calendar.getInstance())) {
//            Toast.makeText(context, "알람 시간이 현재 시간보다 이전입니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
//            Log.e("beforeTime", "before")
//            return
//        }

        val intent = Intent(context, TimeAlarmReceiver::class.java)
        intent.putExtra("timeData", timeData)
        var pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        //알람 설정
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        Log.e("alarmSetting", "alarm")
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
//            calendar.timeInMillis + delay,
//            AlarmManager.INTERVAL_FIFTEEN_MINUTES,
//            pendingIntent)
        Log.e("calendar", "{${calendar.timeInMillis}}")
        alarmManager.set(AlarmManager.RTC, calendar.timeInMillis - calendar2.timeInMillis, pendingIntent)

//        //NotificationChannel, Builder 설정
//        val notificationChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)
//        notificationChannel.enableVibration(true)
//        notificationChannel.enableLights(true)
//        notificationChannel.lightColor = Color.BLUE
//        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC   //content 까지 보여줌
//
//        val builder = NotificationCompat.Builder(context, id)
//            .setSmallIcon(R.drawable.ic_baseline_alarm_24)
//            .setContentTitle(data.title)
//            .setContentText("${data.notifyDateTime!!.month}월 ${data.notifyDateTime!!.dayOfMonth}일 ${data.notifyDateTime!!.hour} 시 ${data.notifyDateTime!!.minute} 분 : [${data.type}]-${data.content}")
//            .setAutoCancel(true)
//
//
//        //Notify 창(헤더 창) 클릭 이벤트
//        val toMainIntent = Intent(context, MainActivity::class.java)
//        toMainIntent.putExtra("timeNotification", data.title)
//        toMainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//
//        val toMainPendingIntent = PendingIntent.getActivity(context, 1, toMainIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//        builder.setContentIntent(toMainPendingIntent)
//
//        //notification 매니저 설정
//        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        manager.createNotificationChannel(notificationChannel)
//
//        val notification = builder.build()
//        manager.notify(10, notification)

    }
}
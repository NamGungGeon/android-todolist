package kr.ac.konkuk.planman

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
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

        //현재일보다 이전이면 등록 실패
//        if (calendar.before(Calendar.getInstance())) {
//            Toast.makeText(context, "알람 시간이 현재 시간보다 이전입니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
//            Log.e("beforeTime", "before")
//            return
//        }

        val intent = Intent(context, TimeAlarmReceiver::class.java)
        val bundle = Bundle()
        bundle.putSerializable("bundleData", timeData)
        intent.putExtra("timeDataByBundle", bundle)
        var pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        //알람 설정
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
//            calendar.timeInMillis + delay,
//            AlarmManager.INTERVAL_FIFTEEN_MINUTES,
//            pendingIntent)
        alarmManager.set(AlarmManager.RTC, calendar.timeInMillis - calendar2.timeInMillis, pendingIntent)

    }
}
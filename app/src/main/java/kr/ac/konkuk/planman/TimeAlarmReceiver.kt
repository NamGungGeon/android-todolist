package kr.ac.konkuk.planman

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.provider.AlarmClock
import android.util.Log

class TimeAlarmReceiver : BroadcastReceiver() {
    lateinit var mediaPlayer : MediaPlayer

    //기기가 다시 시작되면 알람 시작
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("onReceive()", "receiver test")

        /*
        case 1
         */
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val ringtone = RingtoneManager.getRingtone(context, notification)

        ringtone.play()


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
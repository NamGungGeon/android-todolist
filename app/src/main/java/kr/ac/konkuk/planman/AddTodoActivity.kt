package kr.ac.konkuk.planman

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import kr.ac.konkuk.planman.databinding.ActivityAddTodoBinding
import kr.ac.konkuk.planman.databinding.AddTodoCategoryboxBinding
import kr.ac.konkuk.planman.databinding.AddTodoTimepickerBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddTodoActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddTodoBinding
    lateinit var data: MyData

    lateinit var timeNotificationManager: TimeAlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = intent.getSerializableExtra("data") as MyData
        init()
        if (data.title != null)
            initData()
    }

    private fun initSwap(box: AddTodoCategoryboxBinding, content: View) {
        content.isVisible = false
        box.layout.setOnClickListener {
            content.isVisible = !content.isVisible
            if (content.isVisible)
                box.addTodoCategoryUpdown.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            else
                box.addTodoCategoryUpdown.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            box.layout.invalidate()
        }
    }

    private fun init() {
        binding.dropDownWebAddress.addTodoCategoryIcon.setImageResource(R.drawable.ic_baseline_find_in_page_24)
        binding.dropDownWebAddress.addTodoCategoryTitle.text = "웹사이트"
        initSwap(binding.dropDownWebAddress, binding.editTextTextWebAddress)

        binding.dropDownPhoneNumber.addTodoCategoryTitle.text = "전화번호"
        initSwap(binding.dropDownPhoneNumber, binding.editTextPhoneNumber)

        binding.dropDownLocation.addTodoCategoryIcon.setImageResource(R.drawable.ic_baseline_map_24)
        binding.dropDownLocation.addTodoCategoryTitle.text = "장소"
        initSwap(binding.dropDownLocation, binding.mapView)

        binding.textConfirmTime.isVisible = false
        binding.dropDownSetDate.addTodoCategoryIcon.setImageResource(R.drawable.ic_baseline_edit_calendar_24)
        binding.dropDownSetDate.addTodoCategoryTitle.text = "날짜/시간"
        initSwap(binding.dropDownSetDate, binding.calendarView)
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val dlgBinding = AddTodoTimepickerBinding.inflate(layoutInflater)
            val dlgBuilder = AlertDialog.Builder(this)
            dlgBuilder.setView(dlgBinding.root).setPositiveButton("확인") {
                _, _ ->
                data.notifyDateTime = LocalDateTime.of(
                    year + 0,
                    month + 1,  //+1
                    dayOfMonth + 0,
                    dlgBinding.timePicker.hour + 0,
                    dlgBinding.timePicker.minute + 0
                )
                var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")
                var dateTime = data.notifyDateTime
                binding.textConfirmTime.text = dateTime!!.format(dateTimeFormatter)
                binding.textConfirmTime.isVisible = true
            }
                .setNegativeButton("취소") {
                        _, _ ->
                }
                .show()
        }

        binding.dropDownSetLocation.addTodoCategoryIcon.setImageResource(R.drawable.ic_baseline_place_24)
        binding.dropDownSetLocation.addTodoCategoryTitle.text = "거리 반경"
        initSwap(binding.dropDownSetLocation, binding.editTextRadius)

        binding.submitButton.setOnClickListener {

            data.title = binding.editTextTodoTitle.text.toString()
            data.content = binding.editTextTodo.text.toString()
            data.type = "tmp"
            data.webSite = binding.editTextTextWebAddress.text.toString()
            data.location = "tmp"
            data.phoneNumber = binding.editTextPhoneNumber.text.toString()
            data.notifyRadius = binding.editTextRadius.text.toString()

            //시간 예약
//            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//
//            val tIntent = Intent(this, TimeAlarmReceiver::class.java)
//            val calendar = Calendar.getInstance()
//
//            val pendingIntent = PendingIntent.getBroadcast(this, 0, tIntent, 0)

            timeNotificationManager = TimeAlarmManager()
            if (data.notifyDateTime != null) {
                //sendBroadcast(Intent("alarm.test"))
                timeNotificationManager.reservationTimeAlarm(data, this)



//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis
//                    , AlarmManager.INTERVAL_FIFTEEN_MINUTES
//                    , pendingIntent)
            }


            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("data", data) // 나중에 FileIO나 DB로 변경
            startActivity(intent)
            Toast.makeText(this, "할일이 추가되었습니다", Toast.LENGTH_LONG).show()
        }
    }

    private fun initData() {
        binding.editTextTodoTitle.setText(data.title)
        binding.editTextTodo.setText(data.content)
        binding.editTextTextWebAddress.setText(data.webSite)
        binding.editTextPhoneNumber.setText(data.phoneNumber)
    }
}
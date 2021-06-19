package kr.ac.konkuk.planman

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.MotionEvent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kr.ac.konkuk.planman.databinding.ActivityAddTodoBinding
import kr.ac.konkuk.planman.databinding.AddTodoCategoryboxBinding
import kr.ac.konkuk.planman.databinding.AddTodoTimepickerBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddTodoActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddTodoBinding
    lateinit var data: MyData2
    lateinit var googleMap: GoogleMap
    private val seoul = LatLng(37.5547, 126.9706)
    lateinit var pos: LatLng

    lateinit var timeNotificationManager: TimeAlarmManager
    private var selectedCategory: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedCategory = intent.getStringExtra("category")

        data = intent.getSerializableExtra("data") as MyData2
        init()
        if (data.id.toInt() != -1)
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

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        val db = DB(this)
        val categoryDataList = db.readCategory()
        val categories =  ArrayList<String>()
        for (i in categoryDataList) {
            categories.add(i.type.toString())
        }

        //val categories = ArrayList<String>(arrayListOf("업무", "약속", "구매"))
        val spinnerAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        binding.categorySpinner.adapter = spinnerAdapter
        binding.categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedCategory = categories[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
//                    TODO("Not yet implemented")
                }
            }
        if (selectedCategory != null) {
            //find
            val index = categories.indexOf(selectedCategory!!)
            if (index != -1) {
                binding.categorySpinner.setSelection(0)
            }
        }


        binding.dropDownWebAddress.addTodoCategoryIcon.setImageResource(R.drawable.ic_baseline_find_in_page_24)
        binding.dropDownWebAddress.addTodoCategoryTitle.text = "웹사이트"
        initSwap(binding.dropDownWebAddress, binding.editTextTextWebAddress)

        binding.dropDownPhoneNumber.addTodoCategoryTitle.text = "전화번호"
        initSwap(binding.dropDownPhoneNumber, binding.editTextPhoneNumber)

        binding.dropDownLocation.addTodoCategoryIcon.setImageResource(R.drawable.ic_baseline_map_24)
        binding.dropDownLocation.addTodoCategoryTitle.text = "장소"
        initSwap(binding.dropDownLocation, binding.map)

        val transImage = binding.transparentImage
        transImage.setOnTouchListener { _, event ->
            var action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.root.requestDisallowInterceptTouchEvent(true)
                    false
                }
                MotionEvent.ACTION_UP -> {
                    binding.root.requestDisallowInterceptTouchEvent(true)
                    false
                }
                MotionEvent.ACTION_MOVE -> {
                    binding.root.requestDisallowInterceptTouchEvent(true)
                    false
                }
                else -> true
            }
        }

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_frag) as SupportMapFragment
        mapFragment.getMapAsync { it ->
            googleMap = it
            if(data.id.toInt() != -1) {
                val loc = data.attachment.location!!.split(" ")
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(loc[0].toDouble(), loc[1].toDouble()), 11.0f))
                pos = LatLng(loc[0].toDouble(), loc[1].toDouble())
            }
            else
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(seoul, 11.0f))
            googleMap.setMinZoomPreference(8.0f)
            googleMap.setMaxZoomPreference(16.0f)
            googleMap.setOnMapClickListener {
                val option = MarkerOptions()
                option.position(it)
                val category:ArrayList<CategoryData> = db.readCategory()
                val index = category.indexOfFirst {
                    it.type == selectedCategory
                }
                val color = category[index].textColor
                var markerColor = BitmapDescriptorFactory.HUE_CYAN
                if (color == "파랑")
                    markerColor = BitmapDescriptorFactory.HUE_BLUE
                else if (color == "노랑")
                    markerColor = BitmapDescriptorFactory.HUE_YELLOW
                else if (color == "빨강")
                    markerColor = BitmapDescriptorFactory.HUE_RED
                option.icon(BitmapDescriptorFactory.defaultMarker(markerColor)) // 나중에 테마 색으로 바꿀 것
                googleMap.clear()
                googleMap.addMarker(option)
                pos = it
            }
        }


        binding.textConfirmTime.isVisible = false
        binding.dropDownSetDate.addTodoCategoryIcon.setImageResource(R.drawable.ic_baseline_edit_calendar_24)
        binding.dropDownSetDate.addTodoCategoryTitle.text = "날짜/시간"
        initSwap(binding.dropDownSetDate, binding.calendarView)

        var dateTime : LocalDateTime? = null
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val dlgBinding = AddTodoTimepickerBinding.inflate(layoutInflater)
            val dlgBuilder = AlertDialog.Builder(this)
            dlgBuilder.setView(dlgBinding.root).setPositiveButton("확인") { _, _ ->
                data.notification.notifyDateTime = "${year}-${month + 1}-${dayOfMonth}" +
                        "-${dlgBinding.timePicker.hour}-${dlgBinding.timePicker.minute}"


                dateTime = LocalDateTime.of(
                    year,
                    month + 1,
                    dayOfMonth,
                    dlgBinding.timePicker.hour,
                    dlgBinding.timePicker.minute
                )
                var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")
                binding.textConfirmTime.text = dateTime!!.format(dateTimeFormatter)
                binding.textConfirmTime.isVisible = true
            }
                .setNegativeButton("취소") { _, _ ->
                }
                .show()
        }

        binding.dropDownSetLocation.addTodoCategoryIcon.setImageResource(R.drawable.ic_baseline_place_24)
        binding.dropDownSetLocation.addTodoCategoryTitle.text = "거리 반경"
        initSwap(binding.dropDownSetLocation, binding.editTextRadius)

        binding.submitButton.setOnClickListener {

            data.title = binding.editTextTodoTitle.text.toString()
            data.content = binding.editTextTodo.text.toString()
            data.type = selectedCategory
            data.attachment.webSite = binding.editTextTextWebAddress.text.toString()
            try {
                data.attachment.location = "${pos.latitude} ${pos.longitude}"
            } catch (e: kotlin.UninitializedPropertyAccessException) {
                data.attachment.location = ""
            }
            data.attachment.phoneNumber = binding.editTextPhoneNumber.text.toString()
            data.notification.notifyRadius = binding.editTextRadius.text.toString()

            val db = DB(this)
            db.insertMyData(data)

            //시간 예약
            timeNotificationManager = TimeAlarmManager()
            if (dateTime != null) {
                timeNotificationManager.reservationTimeAlarm(data, dateTime,this)
            }


            val intent = Intent(this, MainActivity::class.java)
//            intent.putExtra("data", data) // 나중에 FileIO나 DB로 변경
            startActivity(intent)
            Toast.makeText(this, "할일이 추가되었습니다", Toast.LENGTH_LONG).show()
        }
    }

    private fun initData() {
        binding.editTextTodoTitle.setText(data.title)
        binding.editTextTodo.setText(data.content)
        binding.editTextTextWebAddress.setText(data.attachment.webSite)
        binding.editTextPhoneNumber.setText(data.attachment.phoneNumber)
        val db = DB(this)
        db.deleteMyData(data)
    }
}
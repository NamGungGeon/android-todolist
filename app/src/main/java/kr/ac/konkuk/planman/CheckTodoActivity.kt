package kr.ac.konkuk.planman

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
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
import kr.ac.konkuk.planman.databinding.ActivityCheckTodoBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CheckTodoActivity : AppCompatActivity() {

    lateinit var binding: ActivityCheckTodoBinding
    lateinit var data: MyData2
    lateinit var googleMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = intent.getSerializableExtra("data") as MyData2
        init()
    }

    private fun initText(
        layout: kr.ac.konkuk.planman.databinding.CheckTodoCategoryBinding,
        string: String?
    ) {
        if (string != null)
            layout.checkTodoCategoryContent.text = string
        else
            layout.checkTodoLayout.isVisible = false
    }

    private fun init() {
        binding.title.text = data.title
        binding.checkTodoContent.text = data.title
        supportActionBar?.title = data.title

        binding.checkWebAddress.checkTodoCategoryIcon.setImageResource(R.drawable.ic_baseline_find_in_page_24)
        binding.checkWebAddress.checkTodoCategoryTitle.text = "웹사이트"
        if (data.attachment.webSite == "")
            binding.checkWebAddress.checkTodoLayout.isVisible = false
        else{
            binding.checkWebAddress.checkTodoCategoryContent.text = data.attachment.webSite
            binding.checkWebAddress.checkTodoCategoryContent.setOnClickListener {
                var url= data.attachment.webSite
                if(url == null)
                    return@setOnClickListener
                try{
                    if(!url.contains("http")){
                        url= "http://${url}"
                    }
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(browserIntent)
                }catch (e: Exception){
                    e.printStackTrace()
                    Toast.makeText(applicationContext, "올바른 인터넷 주소가 아닙니다\n${url}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.checkPhoneNumber.checkTodoCategoryTitle.text = "전화번호"
        if (data.attachment.phoneNumber == "")
            binding.checkPhoneNumber.checkTodoLayout.isVisible = false
        else
            binding.checkPhoneNumber.checkTodoCategoryContent.text = data.attachment.phoneNumber

        if (data.attachment.location == "")
            binding.checkTodoMap.isVisible = false
        else {
            if (data.attachment.location == null)
                return

            val location = data.attachment.location!!.split(" ")
            var latLng: LatLng? = null
            try {
                latLng = LatLng(location[0].toDouble(), location[1].toDouble())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val mapFragment =
                supportFragmentManager.findFragmentById(R.id.map_frag2) as SupportMapFragment
            if (latLng == null)
                mapFragment.requireView().visibility = View.GONE
            else
                mapFragment.getMapAsync {
                    googleMap = it
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11.0f))
                    googleMap.setMinZoomPreference(8.0f)
                    googleMap.setMaxZoomPreference(16.0f)

                    val option = MarkerOptions()
                    option.position(latLng)
                    val db = DB(this)
                    val category: ArrayList<CategoryData> = db.readCategory()
                    val index = category.indexOfFirst {
                        it.type == data.type
                    }
                    var markerColor = BitmapDescriptorFactory.HUE_CYAN
                    if(index!= -1){
                        val color = category[index].textColor
                        if (color == "파랑")
                            markerColor = BitmapDescriptorFactory.HUE_BLUE
                        else if (color == "노랑")
                            markerColor = BitmapDescriptorFactory.HUE_YELLOW
                        else if (color == "빨강")
                            markerColor = BitmapDescriptorFactory.HUE_RED
                    }
                    option.icon(BitmapDescriptorFactory.defaultMarker(markerColor))
                    googleMap.addMarker(option)
                }
        }

        binding.checkDateTime.checkTodoCategoryIcon.setImageResource(R.drawable.ic_baseline_edit_calendar_24)
        binding.checkDateTime.checkTodoCategoryContent.text = "날짜/시간"
        if (data.notification.notifyDateTime == null)
            binding.checkDateTime.checkTodoLayout.isVisible = false
        else {
            val date = data.notification.notifyDateTime!!.split("-")
            val dateTime = LocalDateTime.of(
                date[0].toInt(),
                date[1].toInt(),
                date[2].toInt(),
                date[3].toInt(),
                date[4].toInt()
            )
            var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")
            binding.checkDateTime.checkTodoCategoryContent.text = dateTime.format(dateTimeFormatter)
        }

        binding.checkRadius.checkTodoCategoryIcon.setImageResource(R.drawable.ic_baseline_place_24)
        binding.checkRadius.checkTodoCategoryContent.text = "거리 반경"
        if (data.notification.notifyRadius == "")
            binding.checkRadius.checkTodoLayout.isVisible = false
        else
            binding.checkRadius.checkTodoCategoryContent.text = data.notification.notifyRadius

        binding.editButton.setOnClickListener {
            val intent = Intent(applicationContext, AddTodoActivity::class.java)
            intent.putExtra("data", data)
            startActivity(intent)
        }
        binding.removeButton.setOnClickListener{
            AlertDialog.Builder(this@CheckTodoActivity)
                .setTitle("할 일 삭제")
                .setMessage("이 할 일을 삭제합니다\n계속하시겠습니까?")
                .setPositiveButton("삭제") { dialog, i ->
                    DB(applicationContext).deleteMyData(data)
                    dialog.dismiss()
                    finish()
                    Toast.makeText(applicationContext, "삭제 되었습니다", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("닫기") { dialog, i ->
                    dialog.dismiss()
                }.create().show()
        }
    }
}
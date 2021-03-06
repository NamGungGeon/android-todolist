package kr.ac.konkuk.planman

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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

    lateinit var category: ArrayList<CategoryData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = intent.getSerializableExtra("data") as MyData2
        category = DB(this).readCategory()
        if (data.type != null)
            updateActionBarColorWithType()
        init()
    }

    private fun initText(
        layout: kr.ac.konkuk.planman.databinding.CheckTodoCategoryBinding,
        string: String?,
    ) {
        if (string != null)
            layout.checkTodoCategoryContent.text = string
        else
            layout.checkTodoLayout.visibility = View.GONE
    }

    private fun init() {
        binding.title.text = data.title
        binding.checkTodoContent.text = data.title
        supportActionBar?.title = data.title


        binding.checkTodoType.apply {
            if (data.type == null) {
                checkTodoLayout.visibility = View.GONE
            } else {
                checkTodoCategoryIcon.setImageResource(R.drawable.ic_baseline_menu_24)
                checkTodoCategoryIcon.setColorFilter(useCategoryColor())
                checkTodoLayout.visibility = View.VISIBLE
                checkTodoCategoryTitle.also { tv ->
                    tv.setText(data.type)
                    tv.setTextColor(useCategoryColor())
                }
                checkTodoCategoryContent.visibility = View.GONE
            }
        }

        var webSite = data.attachment.webSite ?: ""
        binding.checkWebAddress.checkTodoCategoryIcon.setImageResource(R.drawable.ic_baseline_find_in_page_24)
        binding.checkWebAddress.checkTodoCategoryTitle.text = "????????????"
        if (webSite == "")
            binding.checkWebAddress.checkTodoLayout.visibility = View.GONE
        else {
            binding.checkWebAddress.checkTodoLayout.visibility = View.VISIBLE
            binding.checkWebAddress.checkTodoCategoryContent.text = webSite
        }
        binding.checkWebAddress.checkTodoCategoryContent.setOnClickListener {
            if (webSite == "")
                return@setOnClickListener
            try {
                if (!webSite.contains("http")) {
                    webSite = "http://${webSite}"
                }
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(webSite))
                startActivity(browserIntent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    applicationContext,
                    "????????? ????????? ????????? ????????????\n${webSite}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.checkPhoneNumber.checkTodoCategoryTitle.text = "????????????"
        val phoneNumber = data.attachment.phoneNumber ?: ""
        if (phoneNumber == "")
            binding.checkPhoneNumber.checkTodoLayout.visibility = View.GONE
        else {
            binding.checkPhoneNumber.checkTodoLayout.visibility = View.VISIBLE
            binding.checkPhoneNumber.checkTodoCategoryContent.text = phoneNumber
        }
        binding.checkPhoneNumber.checkTodoLayout.setOnClickListener {
            if (phoneNumber != "") {
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null))
                startActivity(intent)
            }
        }

        if (data.attachment.location == "")
            binding.checkTodoMap.visibility = View.GONE
        else {
            if (data.attachment.location == null) {
                binding.checkTodoMap.visibility = View.GONE
                return
            }

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
                binding.checkTodoMap.visibility = View.GONE
            else
                mapFragment.getMapAsync {
                    binding.checkTodoMap.visibility = View.VISIBLE

                    googleMap = it
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11.0f))
                    googleMap.setMinZoomPreference(8.0f)
                    googleMap.setMaxZoomPreference(16.0f)

                    val option = MarkerOptions()
                    option.position(latLng)
                    val db = DB(this)
                    val index = category.indexOfFirst {
                        it.type == data.type
                    }
                    var markerColor = BitmapDescriptorFactory.HUE_CYAN
                    if (index != -1) {
                        val color = category[index].textColor
                        if (color == "??????")
                            markerColor = BitmapDescriptorFactory.HUE_BLUE
                        else if (color == "??????")
                            markerColor = BitmapDescriptorFactory.HUE_YELLOW
                        else if (color == "??????")
                            markerColor = BitmapDescriptorFactory.HUE_RED
                    }
                    option.icon(BitmapDescriptorFactory.defaultMarker(markerColor))
                    googleMap.addMarker(option)
                }
        }

        binding.checkDateTime.checkTodoCategoryIcon.setImageResource(R.drawable.ic_baseline_edit_calendar_24)
        binding.checkDateTime.checkTodoCategoryTitle.text = "??????/??????"
        if (data.notification.notifyDateTime == null)
            binding.checkDateTime.checkTodoLayout.visibility = View.GONE
        else {
            binding.checkDateTime.checkTodoLayout.visibility = View.VISIBLE
            val date = data.notification.notifyDateTime!!.split("-")
            val dateTime = LocalDateTime.of(
                date[0].toInt(),
                date[1].toInt(),
                date[2].toInt(),
                date[3].toInt(),
                date[4].toInt()
            )
            var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy??? MM??? dd??? HH??? mm???")
            binding.checkDateTime.checkTodoCategoryContent.text = dateTime.format(dateTimeFormatter)
        }

        binding.checkRadius.checkTodoCategoryIcon.setImageResource(R.drawable.ic_baseline_place_24)
        binding.checkRadius.checkTodoCategoryTitle.text = "?????? ??????"
        if (data.notification.notifyRadius == "")
            binding.checkRadius.checkTodoLayout.visibility = View.GONE
        else {
            binding.checkRadius.checkTodoLayout.visibility = View.VISIBLE
            binding.checkRadius.checkTodoCategoryContent.text = data.notification.notifyRadius
        }

        binding.editButton.setOnClickListener {
            val intent = Intent(applicationContext, AddTodoActivity::class.java)
            intent.putExtra("data", data)
            startActivityForResult(intent, 1)
        }
        binding.removeButton.setOnClickListener {
            AlertDialog.Builder(this@CheckTodoActivity)
                .setTitle("??? ??? ??????")
                .setMessage("??? ??? ?????? ???????????????\n?????????????????????????")
                .setPositiveButton("??????") { dialog, i ->
                    DB(applicationContext).deleteMyData(data)
                    dialog.dismiss()
                    finish()
                    Toast.makeText(applicationContext, "?????? ???????????????", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("??????") { dialog, i ->
                    dialog.dismiss()
                }.create().show()
        }
    }

    @ColorInt
    private fun useCategoryColor(): Int {
        return resources.getColor(
            CategoryData.Color.useColor(
                category.find {
                    it.type == data.type
                }?.textColor
            )
        )
    }

    private fun updateActionBarColorWithType() {
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(useCategoryColor())
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val updatedData = DB(this).readMyData(this.data.id.toInt())
        if (updatedData != null) {
            this.data = updatedData
            updateActionBarColorWithType()
            init()
        }
    }
}
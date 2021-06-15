package kr.ac.konkuk.planman

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapTodo(context: Context) {
    var mapFragment = SupportMapFragment.newInstance()
    lateinit var googleMap: GoogleMap
    var data: ArrayList<MyData2> = ArrayList()

    private val seoul = LatLng(37.5547, 126.9706)

    init {
        val db = DB(context)
        data = db.readMyData()

        mapFragment.getMapAsync { it ->
            googleMap = it
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(seoul, 11.0f))
            googleMap.setMinZoomPreference(8.0f)
            googleMap.setMaxZoomPreference(16.0f)
            googleMap.setOnMarkerClickListener {
                val intent= Intent(context, CheckTodoActivity::class.java)
                intent.putExtra("data", it.tag as MyData2)
                context.startActivity(intent)
                true
            }
            for(d in data) {
                if(d.attachment.location != null) {
                    val option = MarkerOptions()
                    val loc = d.attachment.location!!.split(" ")
                    option.position(LatLng(loc[0].toDouble(), loc[1].toDouble()))
                    option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)) //나중에 테마 색으로 바꿀 것
                    option.title(d.title)
                    val marker = googleMap.addMarker(option)
                    marker.showInfoWindow()
                    marker.tag = d
                }
            }
        }
    }

}
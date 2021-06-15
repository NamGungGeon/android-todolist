package kr.ac.konkuk.planman

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MapTodo {
    val mapFragment = SupportMapFragment.newInstance()
    lateinit var googleMap: GoogleMap

    private val seoul = LatLng(37.5547, 126.9706)

    init {
        mapFragment.getMapAsync {
            googleMap = it
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(seoul, 16.0f))
        }
    }

}
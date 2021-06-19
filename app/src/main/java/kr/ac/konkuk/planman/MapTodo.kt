package kr.ac.konkuk.planman

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LifecycleObserver
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.lang.NumberFormatException

class MapTodo(val context: Context, val filterTodoViewModel: FilterTodoViewModel) {
    var mapFragment = SupportMapFragment.newInstance()
    lateinit var googleMap: GoogleMap
    var data: ArrayList<MyData2> = ArrayList()

    private val seoul = LatLng(37.5547, 126.9706)
    var category: ArrayList<CategoryData>
    private val markers = ArrayList<Marker>()

    init {
        val db = DB(context)
        data = db.readMyData()

        category = db.readCategory()
    }

    fun init() {
        mapFragment.getMapAsync { it ->
            googleMap = it
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(seoul, 11.0f))
            googleMap.setMinZoomPreference(8.0f)
            googleMap.setMaxZoomPreference(16.0f)
            googleMap.setOnMarkerClickListener {
                val intent = Intent(context, CheckTodoActivity::class.java)
                intent.putExtra("data", it.tag as MyData2)
                context.startActivity(intent)
                true
            }
            markers.map { marker ->
                marker.remove()
            }
            markers.clear()

            val searchKeyword = filterTodoViewModel.searchKeyword.value
            val selectedCategory = filterTodoViewModel.selectedCategory.value
            val filteredCategory= ArrayList(data.filter { todo ->
                if (searchKeyword?.isNotEmpty() == true)
                    (todo.title != null && todo.title!!.contains(searchKeyword.toString()))
                            || (todo.content != null && todo.content!!.contains(searchKeyword.toString()))
                else
                    if (selectedCategory != null)
                        todo.type != null && todo.type!! == selectedCategory
                    else true
            }.toList())
            for (d in filteredCategory) {
                if (d.attachment.location != null && d.attachment.location != "") {
                    val option = MarkerOptions()
                    val loc = d.attachment.location!!.split(" ")
                    val index = category.indexOfFirst {
                        it.type == d.type
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

                    try {
                        option.position(LatLng(loc[0].toDouble(), loc[1].toDouble()))
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                        continue
                    }
                    option.icon(BitmapDescriptorFactory.defaultMarker(markerColor)) //나중에 테마 색으로 바꿀 것
                    option.title(d.title)
                    val marker = googleMap.addMarker(option)
                    //marker.showInfoWindow()
                    marker.tag = d
                    markers.add(marker)
                }
            }
        }
    }
}
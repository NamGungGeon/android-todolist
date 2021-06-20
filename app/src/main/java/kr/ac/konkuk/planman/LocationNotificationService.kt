package kr.ac.konkuk.planman

import android.app.Activity
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class LocationNotificationService : Service() {
    companion object {
        var isRunning = false

        //10 mins
        private val CHECK_INTERVAL: Long = 10 * 60 * 1000

        fun start(activity: Activity) {
            if (!isRunning)
                activity.startService(Intent(activity, LocationNotificationService::class.java))
        }
    }

    private val binder = object : Binder() {
        fun getService(): Binder {
            return this
        }
    }
    private val observer = Thread {
        isRunning = true
        val handler = Handler(Looper.getMainLooper())
        while (true) {
            try {
                handler.post {
                    LocationManager.getInstance().useLastLocation(this) { location ->
                        //scan todolist
                        if (location == null)
                            return@useLastLocation

                        //location is available
                        val todoList = DB(this).readMyData()
                        val notificationTargetList = ArrayList<MyData2>()
                        todoList.map { todo ->
                            //calc distance between current location and todolist location
                            try{
                                val latlng= todo.attachment.location!!.split(" ")
                                Log.i("locaiton check", "${todo.title}: ${todo.attachment.location!!}")
                                val lat = latlng[0].toDouble()
                                val lng = latlng[1].toDouble()
                                if (lat != 0.0 && lng != 0.0) {
                                    val distanceFromHere =
                                        distance(lat, lng, location.latitude, location.longitude)
                                    if (distanceFromHere <= 0.3) {
                                        notificationTargetList.add(todo)
                                    }
                                }
                            }catch (e: Exception){
                                e.printStackTrace()
                            }
                        }

                        if (notificationTargetList.isNotEmpty()) {
                            notificationTargetList.map { todo ->
                                PlanmanNotificationManager.getInstance()
                                    .sendNotification(this@LocationNotificationService, todo)
                            }
                        }
//                        //test
//                        NotificationManager.getInstance()
//                            .sendNotification(
//                                this@LocationNotificationService,
//                                MyData2()
//                            )
                    }
                }
                Thread.sleep(CHECK_INTERVAL)
            } catch (e: InterruptedException) {
                e.printStackTrace()
                break
            }
        }
        isRunning = false
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        observer.start()
    }

    private fun distance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double,
        unit: Char = 'K',
    ): Double {
        val theta = lon1 - lon2
        var dist =
            sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + cos(deg2rad(lat1)) * cos(deg2rad(lat2)) * cos(
                deg2rad(theta)
            )
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        if (unit == 'K') {
            dist *= 1.609344
        } else if (unit == 'N') {
            dist *= 0.8684
        }
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    override fun onDestroy() {
        super.onDestroy()
        observer.interrupt()
    }
}
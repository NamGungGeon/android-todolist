package kr.ac.konkuk.planman

import android.content.ContentValues
import java.io.Serializable

class MyData2 : Serializable {
    var id: Long = -1
    var title: String? = null
    var content: String? = null
    var type: String? = null
    var attachment: Attachment = Attachment()
    var notification: Notification = Notification()

    class Attachment : Serializable {
        var webSite: String? = null
        var phoneNumber: String? = null
        var location: String? = null
        var locationLat: Double= 0.0
        var locationLng: Double= 0.0
    }

    class Notification : Serializable {
        var notifyDateTime: String? = null
        var notifyRadius: String? = null
    }

    fun getContentValues() : ContentValues{
        val cv = ContentValues()
        cv.put("title", title)
        cv.put("content", content)
        cv.put("type", type)
        cv.put("att_webSite", attachment.webSite)
        cv.put("att_phoneNumber", attachment.phoneNumber)
        cv.put("att_location", attachment.location)
        cv.put("not_notifyDateTime", notification.notifyDateTime)
        cv.put("not_notifyRadius", notification.notifyRadius)
        return cv
    }
}
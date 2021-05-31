package kr.ac.konkuk.planman

import java.io.Serializable
import java.time.LocalDateTime

class MyData2(
    var title: String?,
    var content: String?,
    var type: String?,
    var attachment: Attachment?,
    var notification: Notification?,
) : Serializable {
    class Attachment(
        var webSite: String?,
        var phoneNumber: String?,
        var location: String?,
    ) : Serializable

    class Notification(
        var notifyDateTime: LocalDateTime?,
        var notifyRadius: String?
    ) : Serializable
}
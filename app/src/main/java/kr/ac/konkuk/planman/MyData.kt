package kr.ac.konkuk.planman

import java.io.Serializable
import java.time.LocalDateTime

data class MyData(
    var title: String?,
    var content: String?,
    var type: String?,
    var webSite: String?,
    var phoneNumber: String?,
    var location: String?,
    var notifyDateTime: LocalDateTime?,
    var notifyRadius: String?
) : Serializable {
    constructor() : this(null, null, null, null, null, null, null, null)
}
package kr.ac.konkuk.planman

import android.content.ContentValues
import java.io.Serializable
import java.time.format.TextStyle

//textSize : 크게, 보통, 작게
//textColor : 파랑, 노랑, 빨강, 검정
//textStyle : 진하게, 보통, 이탤릭체
class CategoryData : Serializable {
    var id: Long = -1
    var type:String? = null
    var textSize:String? = null
    var textColor:String? = null
    var textStyle:String? = null

    constructor()

    constructor(type : String?, textSize:String?, textColor : String?, textStyle: String?) {
        this.type = type
        this.textSize = textSize
        this.textColor = textColor
        this.textStyle = textStyle
    }

    constructor(id : Long, type : String?, textSize:String?, textColor : String?, textStyle: String?) {
        this.id = id
        this.type = type
        this.textSize = textSize
        this.textColor = textColor
        this.textStyle = textStyle
    }

    fun getContentValues() : ContentValues {
        val cv = ContentValues()
        cv.put("type", type)
        cv.put("textSize", textSize)
        cv.put("textColor", textColor)
        cv.put("textStyle", textStyle)

        return cv
    }
}
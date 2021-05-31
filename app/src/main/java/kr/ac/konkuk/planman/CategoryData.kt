package kr.ac.konkuk.planman

import java.io.Serializable

//textSize : 크게, 보통, 작게
//textColor : 파랑, 노랑, 빨강, 검정
//textStyle : 진하게, 보통, 이탤릭체
data class CategoryData(var type:String, var textSize:String, var textColor:String, var textStyle:String):Serializable {
}
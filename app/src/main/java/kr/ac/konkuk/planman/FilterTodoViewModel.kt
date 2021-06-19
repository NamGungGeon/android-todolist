package kr.ac.konkuk.planman

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FilterTodoViewModel: ViewModel() {
    val selectedCategory= MutableLiveData<String?>()
    val searchKeyword= MutableLiveData<String?>("")

    fun setSelectedCategory(category: String?){
        selectedCategory.value= category
    }
    fun setSearchKeyword(keyword: String){
        searchKeyword.value= keyword
    }
}
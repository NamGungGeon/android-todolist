package kr.ac.konkuk.planman

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SelectedCategoryViewModel: ViewModel() {
    val selectedCategory= MutableLiveData<String?>()

    fun setSelectedCategory(category: String?){
        selectedCategory.value= category
    }
}
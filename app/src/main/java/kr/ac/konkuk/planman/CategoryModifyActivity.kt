package kr.ac.konkuk.planman

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import kr.ac.konkuk.planman.databinding.ActivityCategoryModifyBinding

class CategoryModifyActivity : AppCompatActivity() {
    lateinit var binding: ActivityCategoryModifyBinding
    lateinit var textSizeSpinner2 : Spinner
    lateinit var textColorSpinner2 : Spinner
    lateinit var textStyleSpinner2 : Spinner

    lateinit var categoryName : String

    lateinit var inputTextSize : String
    lateinit var inputTextColor : String
    lateinit var inputTextStyle : String

    var data : ArrayList<CategoryData> = ArrayList()

    lateinit var getData : CategoryData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        getData = intent.getSerializableExtra("category") as CategoryData
        initSpinner()
        initTitle()
        initPreView()
    }


    private fun initTitle() {
        //intent로 type 이름 가져와서 title 바꾼다
        val type : String = getData.type
        binding.categoryTitleText.text = "$type 카테고리"
    }



    private fun initSpinner() {
        textSizeSpinner2 = findViewById(R.id.text_size_spinner2)
        textSizeSpinner2.adapter = ArrayAdapter.createFromResource(this, R.array.category_text_size
            , android.R.layout.simple_spinner_dropdown_item)

        textColorSpinner2 = findViewById(R.id.text_color_spinner2)
        textColorSpinner2.adapter = ArrayAdapter.createFromResource(this, R.array.category_text_color
            , android.R.layout.simple_spinner_dropdown_item)

        textStyleSpinner2 = findViewById(R.id.text_style_spinner2)
        textStyleSpinner2.adapter = ArrayAdapter.createFromResource(this, R.array.category_text_style
            , android.R.layout.simple_spinner_dropdown_item)
    }

    private fun initPreView() {
        binding.apply {
            categoryName = getData.type
            inputTextSize = getData.textSize
            inputTextColor = getData.textColor
            inputTextStyle = getData.textStyle

            data.add(CategoryData(categoryName, inputTextSize, inputTextColor, inputTextStyle))

            categoryPreview2.layoutManager = LinearLayoutManager(this@CategoryModifyActivity, LinearLayoutManager.VERTICAL, false)
            categoryPreview2.adapter = CategoryModifyAdapter(data)

        }
    }
}
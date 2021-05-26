package kr.ac.konkuk.planman

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import kr.ac.konkuk.planman.databinding.ActivityCategoryAddBinding

class CategoryAddActivity : AppCompatActivity() {
    lateinit var textSizeSpinner : Spinner
    lateinit var textColorSpinner : Spinner
    lateinit var textStyleSpinner : Spinner
    lateinit var binding : ActivityCategoryAddBinding

    lateinit var previewText : TextView

    lateinit var inputTextSize : String
    lateinit var inputTextColor : String
    lateinit var inputTextStyle : String

    lateinit var categoryName : String

    lateinit var data: ArrayList<CategoryData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        initSpinner()
        initPreview()
        createCategory()

    }

    private fun initSpinner() {
        textSizeSpinner = findViewById(R.id.text_size_spinner)
        textSizeSpinner.adapter = ArrayAdapter.createFromResource(this, R.array.category_text_size
            , android.R.layout.simple_spinner_dropdown_item)

        textColorSpinner = findViewById(R.id.text_color_spinner)
        textColorSpinner.adapter = ArrayAdapter.createFromResource(this, R.array.category_text_color
            , android.R.layout.simple_spinner_dropdown_item)

        textStyleSpinner = findViewById(R.id.text_style_spinner)
        textStyleSpinner.adapter = ArrayAdapter.createFromResource(this, R.array.category_text_style
            , android.R.layout.simple_spinner_dropdown_item)
    }

    private fun initPreview() {
        binding.apply {
            categoryName = categoryNameText.text.toString()
            inputTextSize = textSizeSpinner.selectedItem.toString()
            inputTextColor = textColorSpinner.selectedItem.toString()
            inputTextStyle = textStyleSpinner.selectedItem.toString()

            data = ArrayList()
            data.add(CategoryData(categoryName, inputTextSize, inputTextColor, inputTextStyle))

            categoryPreview1.layoutManager = LinearLayoutManager(this@CategoryAddActivity, LinearLayoutManager.VERTICAL, false)
            categoryPreview1.adapter = CategoryAddAdapter(data)
        }

    }

    private fun createCategory() {

    }
}
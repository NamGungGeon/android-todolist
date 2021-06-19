package kr.ac.konkuk.planman

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
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

    val db = DB(this)
    var data: ArrayList<CategoryData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title= "카테고리 추가"

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

        textSizeSpinner.setSelection(1) //보통
        textColorSpinner.setSelection(0)    //파랑
        textStyleSpinner.setSelection(1)    //보통

        textSizeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position) {
                    0 ->  inputTextSize = "크게"
                    1 -> inputTextSize = "보통"
                    2 -> inputTextSize = "작게"
                }
                data.clear()
                data.add(CategoryData(categoryName, inputTextSize, inputTextColor, inputTextStyle))
                binding!!.categoryPreviewAdd.adapter = CategoryModifyAdapter(data)
            }
        }

        textColorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position) {
                    0 -> inputTextColor = "파랑"
                    1 -> inputTextColor = "노랑"
                    2 -> inputTextColor = "빨강"
                    3 -> inputTextColor = "검정"
                }
                data.clear()
                data.add(CategoryData(categoryName, inputTextSize, inputTextColor, inputTextStyle))
                binding!!.categoryPreviewAdd.adapter = CategoryModifyAdapter(data)
            }
        }

        textStyleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position) {
                    0 -> inputTextStyle = "진하게"
                    1 -> inputTextStyle = "보통"
                    2 -> inputTextStyle = "이탤릭체"
                }
                data.clear()
                data.add(CategoryData(categoryName, inputTextSize, inputTextColor, inputTextStyle))
                binding!!.categoryPreviewAdd.adapter = CategoryModifyAdapter(data)
            }

        }
    }

    private fun initPreview() {
        binding.apply {
            categoryName = categoryNameEditTextAdd.text.toString()
            inputTextSize = textSizeSpinner.selectedItem.toString()
            inputTextColor = textColorSpinner.selectedItem.toString()
            inputTextStyle = textStyleSpinner.selectedItem.toString()

            categoryNameEditTextAdd.addTextChangedListener {
                categoryName = it.toString()
                data.clear()
                data.add(CategoryData(categoryName, inputTextSize, inputTextColor, inputTextStyle))
                categoryPreviewAdd.adapter = CategoryAddAdapter(data)
            }


            data.add(CategoryData(categoryName, inputTextSize, inputTextColor, inputTextStyle))

            categoryPreviewAdd.layoutManager = LinearLayoutManager(this@CategoryAddActivity, LinearLayoutManager.VERTICAL, false)
            categoryPreviewAdd.adapter = CategoryAddAdapter(data)
        }

    }

    private fun createCategory() {
        binding!!.categoryAddTodoBtnAdd.setOnClickListener {
            //DB insert category data
            db.insertCategory(CategoryData(categoryName, inputTextSize, inputTextColor, inputTextStyle))

            val intent = Intent()
            intent.putExtra("addCategoryData", CategoryData(categoryName, inputTextSize, inputTextColor, inputTextStyle))
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
package kr.ac.konkuk.planman

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.ListFragment
import androidx.recyclerview.widget.LinearLayoutManager
import kr.ac.konkuk.planman.databinding.ActivityCategoryModifyBinding

class CategoryModifyActivity : AppCompatActivity() {
    lateinit var binding: ActivityCategoryModifyBinding
    lateinit var textSizeSpinner2 : Spinner
    lateinit var textColorSpinner2 : Spinner
    lateinit var textStyleSpinner2 : Spinner

    var categoryName : String? = null
    var inputTextSize : String? = null
    var inputTextColor : String? = null
    var inputTextStyle : String? = null

    var data : ArrayList<CategoryData> = ArrayList()
    val db = DB(this)
    var listTodoFragment = ListTodoFragment()

    lateinit var getData : CategoryData
    lateinit var getDataNotModified : CategoryData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        getData = intent.getSerializableExtra("category") as CategoryData
        getDataNotModified = CategoryData(getData.id, getData.type, getData.textSize, getData.textColor, getData.textStyle)
        initSpinner()
        initTitle()
        initPreView()

        binding!!.apply {
            deleteImg.setOnClickListener {
                //DB delete category
                if (listTodoFragment.isUsedCategoryData(getData)) {     //리스트 프래그먼트에서 해당 카테고리 내용 사용 중일 경우
                    listTodoFragment.setDefaultCategoryType(getData)    //해당 데이터의 type = null 로 설정해 디폴트 타입으로 바꿔준다.
                    db.deleteCategory(getData)
                } else {
                    db.deleteCategory(getData)
                }

                val intent = Intent()
                setResult(Activity.RESULT_CANCELED, intent)
                finish()
            }

            categoryModifyTodoBtn.setOnClickListener {
                if (listTodoFragment.isUsedCategoryData(getDataNotModified)) { //리스트 프래그먼트에서 해당 카테고리 내용 사용 중일 경우
                    listTodoFragment.updateExistingCategoryData(getData, getDataNotModified)
                    db.updateCategory(getData)
                } else {
                    db.updateCategory(getData)
                }

                val intent = Intent()
                intent.putExtra("modifyTodo", CategoryData(categoryName, inputTextSize, inputTextColor, inputTextStyle))
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }


    private fun initTitle() {
        //intent로 type 이름 가져와서 title 바꾼다
        val type : String? = getData.type
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

        textSizeSpinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                binding!!.categoryPreviewModify.adapter = CategoryModifyAdapter(data)

                getData.textSize = inputTextSize
            }
        }

        textColorSpinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                binding!!.categoryPreviewModify.adapter = CategoryModifyAdapter(data)

                getData.textColor = inputTextColor
            }
        }

        textStyleSpinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                binding!!.categoryPreviewModify.adapter = CategoryModifyAdapter(data)

                getData.textStyle = inputTextStyle
            }

        }
    }

    private fun initPreView() {
        binding.apply {
            categoryName = getData.type
            inputTextSize = getData.textSize
            inputTextColor = getData.textColor
            inputTextStyle = getData.textStyle

            /*
            초기 설정
             */
            if (inputTextSize == "크게") {
                textSizeSpinner2.setSelection(0)
            } else if (inputTextSize == "보통") {
                textSizeSpinner2.setSelection(1)
            } else {    //작게
                textSizeSpinner2.setSelection(2)
            }

            if (inputTextColor == "파랑") {
                textColorSpinner2.setSelection(0)
            } else if (inputTextColor == "노랑") {
                textColorSpinner2.setSelection(1)
            } else if (inputTextColor == "빨강") {
                textColorSpinner2.setSelection(2)
            } else {        //검정
                textColorSpinner2.setSelection(3)
            }

            if (inputTextStyle == "진하게") {
                textStyleSpinner2.setSelection(0)
            } else if (inputTextStyle == "보통") {
                textStyleSpinner2.setSelection(1)
            } else {    //작게
                textStyleSpinner2.setSelection(2)
            }

            categoryNameEditTextModify.setText(categoryName)

            categoryNameEditTextModify.addTextChangedListener {
                categoryName = it.toString()
                data.clear()
                data.add(CategoryData(categoryName, inputTextSize, inputTextColor, inputTextStyle))
                binding!!.categoryPreviewModify.adapter = CategoryModifyAdapter(data)

                getData.type = it.toString()
            }

            data.add(CategoryData(categoryName, inputTextSize, inputTextColor, inputTextStyle))

            categoryPreviewModify.layoutManager = LinearLayoutManager(this@CategoryModifyActivity, LinearLayoutManager.VERTICAL, false)
            categoryPreviewModify.adapter = CategoryModifyAdapter(data)

        }
    }
}
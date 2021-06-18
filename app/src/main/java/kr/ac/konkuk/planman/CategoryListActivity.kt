package kr.ac.konkuk.planman

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kr.ac.konkuk.planman.databinding.ActivityCategoryListBinding

class CategoryListActivity : AppCompatActivity() {

    private val ADD_TO_DO_REQUEST_CODE = 100
    private val MODIFY_TO_DO_REQUEST_CODE = 200
    private var selectedItemPosition : Int = 0

    lateinit var categoryData:ArrayList<CategoryData>
    lateinit var binding: ActivityCategoryListBinding
    lateinit var adapter: CategoryListAdapter

    val db = DB(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        initAddBtn()
        initCategoryListData()
        initCategoryListRecyclerView()
    }

    private fun initAddBtn() {
        binding.apply {
            categoryAddTodoBtn.setOnClickListener {
                val intent= Intent(this@CategoryListActivity, CategoryAddActivity::class.java)
                startActivityForResult(intent, ADD_TO_DO_REQUEST_CODE)
            }
        }
    }

    private fun initCategoryListData() {
        //textSize : 크게, 보통, 작게
        //textColor : 검정, 파랑, 노랑, 빨강 등
        //textStyle : 일반, 진하게, 이탤릭체
        categoryData = db.readCategory()
    }

    private fun initCategoryListRecyclerView() {
        binding.apply {
            categoryList.layoutManager = LinearLayoutManager(this@CategoryListActivity, LinearLayoutManager.VERTICAL, false)

            adapter = CategoryListAdapter(categoryData)
            adapter.itemClickListener = object : CategoryListAdapter.OnItemClickListener {
                override fun OnItemClick(
                    holder: CategoryListAdapter.ViewHolder,
                    view: View,
                    data: CategoryData,
                    position: Int
                ) {
                    selectedItemPosition = position
                    val intent = Intent(this@CategoryListActivity, CategoryModifyActivity::class.java)
                    intent.putExtra("category", data)
                    startActivityForResult(intent, MODIFY_TO_DO_REQUEST_CODE)   //startActivityForResult
                }
            }
            categoryList.adapter = adapter
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            ADD_TO_DO_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val getData = data?.getSerializableExtra("addCategoryData") as CategoryData
                    categoryData.add(getData)
                    adapter = CategoryListAdapter(categoryData)
                    adapter.itemClickListener = object : CategoryListAdapter.OnItemClickListener {
                        override fun OnItemClick(
                                holder: CategoryListAdapter.ViewHolder,
                                view: View,
                                data: CategoryData,
                                position: Int
                        ) {
                            selectedItemPosition = position
                            val intent = Intent(this@CategoryListActivity, CategoryModifyActivity::class.java)
                            intent.putExtra("category", data)
                            //Log.e("test", "success")
                            startActivityForResult(intent, MODIFY_TO_DO_REQUEST_CODE)   //startActivityForResult
                        }
                    }
                    binding!!.categoryList.adapter = adapter
                }
            }

            MODIFY_TO_DO_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_CANCELED) {       //삭제 될때
                    categoryData = db.readCategory()
                    adapter = CategoryListAdapter(categoryData)
                    adapter.itemClickListener = object : CategoryListAdapter.OnItemClickListener {
                        override fun OnItemClick(
                                holder: CategoryListAdapter.ViewHolder,
                                view: View,
                                data: CategoryData,
                                position: Int
                        ) {
                            selectedItemPosition = position
                            val intent = Intent(this@CategoryListActivity, CategoryModifyActivity::class.java)
                            intent.putExtra("category", data)
                            //Log.e("test", "success")
                            startActivityForResult(intent, MODIFY_TO_DO_REQUEST_CODE)   //startActivityForResult
                        }

                    }
                    binding!!.categoryList.adapter = adapter
                }
                else if (resultCode == Activity.RESULT_OK) {    //수정 될때
                    val getDataFromModify = data?.getSerializableExtra("modifyTodo") as CategoryData


                    //DB update 부분
                    categoryData = db.readCategory()

//                    categoryData[selectedItemPosition].type = getDataFromModify.type
//                    categoryData[selectedItemPosition].textSize = getDataFromModify.textSize
//                    categoryData[selectedItemPosition].textColor = getDataFromModify.textColor
//                    categoryData[selectedItemPosition].textStyle = getDataFromModify.textStyle

                    adapter = CategoryListAdapter(categoryData)

                    adapter.itemClickListener = object : CategoryListAdapter.OnItemClickListener {
                        override fun OnItemClick(
                                holder: CategoryListAdapter.ViewHolder,
                                view: View,
                                data: CategoryData,
                                position: Int
                        ) {
                            selectedItemPosition = position
                            val intent = Intent(this@CategoryListActivity, CategoryModifyActivity::class.java)
                            intent.putExtra("category", data)
                            //Log.e("test", "success")
                            startActivityForResult(intent, MODIFY_TO_DO_REQUEST_CODE)   //startActivityForResult
                        }
                    }

                    binding!!.categoryList.adapter = adapter
                }
            }
        }

    }
}
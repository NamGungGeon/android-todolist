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

    private val ADD_TO_DO_REQUEST = 100
    private val MODIFY_TO_DO_REQUEST = 200

    var categoryData:ArrayList<CategoryData> = ArrayList()
    lateinit var binding: ActivityCategoryListBinding
    lateinit var adapter: CategoryListAdapter

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
            categoryAddTodoBtn1.setOnClickListener {
                val intent= Intent(this@CategoryListActivity, CategoryAddActivity::class.java)
                startActivityForResult(intent, ADD_TO_DO_REQUEST)
            }
        }
    }

    private fun initCategoryListData() {
        //textSize : 크게, 보통, 작게
        //textColor : 검정, 파랑, 노랑, 빨강 등
        //textStyle : 일반, 진하게, 이탤릭체
        categoryData.add(CategoryData("업무", "보통", "파랑", "보통"))
        categoryData.add(CategoryData("약속", "보통", "노랑", "보통"))
        categoryData.add(CategoryData("구매", "보통", "빨강", "보통"))
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
                    val intent = Intent(this@CategoryListActivity, CategoryModifyActivity::class.java)
                    intent.putExtra("category", data)
                    //Log.e("test", "success")
                    startActivityForResult(intent, MODIFY_TO_DO_REQUEST)   //startActivityForResult
                }

            }
            categoryList.adapter = adapter
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        //resultCode에 따라서 intent에 실린 값 받아와서 실행하게끔한다.
//    }
}
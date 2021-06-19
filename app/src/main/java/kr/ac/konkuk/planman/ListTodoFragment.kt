package kr.ac.konkuk.planman

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.ac.konkuk.planman.databinding.FragmentListTodoBinding
import java.time.LocalDateTime

class ListTodoFragment : Fragment() {
    var data: ArrayList<MyData2> = ArrayList()
    var categoryDataList: ArrayList<CategoryData> = ArrayList()
    var usingCategoryTypeList : ArrayList<String?> = ArrayList()

    lateinit var recyclerView: RecyclerView
    var binding: FragmentListTodoBinding? = null
    lateinit var adapter : ListAdapter

    val db = activity?.applicationContext?.let { DB(it) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListTodoBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()

        initData()
        initRecyclerView()
    }

    private fun initData() {
        val db = DB(requireContext())
        data = db.readMyData()

        //사용중인 카테고리 타입 리스트 가져온다.
        for (i in data) {
            usingCategoryTypeList.add(i.type)
        }
    }

    fun setCustomData(data: ArrayList<MyData2>) {
        this.data = data
        initRecyclerView()
    }

    fun isUsedCategoryData(categoryData: CategoryData) : Boolean {
        for (i in usingCategoryTypeList) {
            if (i == categoryData.type) {
                return true
            }
        }
        return false
    }

    fun setDefaultCategoryType(categoryData: CategoryData) {
        data = db!!.readMyData()

        for (i in data) {
            if (i.type == categoryData.type) {
                i.type = null
            }
        }
    }

    fun updateExistingCategoryData(categoryData: CategoryData, notModifiedCategoryData: CategoryData) {
        data = db!!.readMyData()

        for (i in data) {
            if (i.type == notModifiedCategoryData.type) {
                i.type = categoryData.type
                db.updateMyData(i)
            }
        }
    }


    private fun initRecyclerView() {
        adapter = ListAdapter(data)
        adapter.itemClickListener = object : ListAdapter.OnItemClickListener {
            override fun OnItemClick(
                holder: ListAdapter.ViewHolder,
                view: View,
                data: MyData2,
                position: Int
            ) {
                val intent = Intent(activity, CheckTodoActivity::class.java)
                intent.putExtra("data", data)
                startActivity(intent)
            }
        }
        recyclerView = requireView().findViewById<RecyclerView>(R.id.list_recyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
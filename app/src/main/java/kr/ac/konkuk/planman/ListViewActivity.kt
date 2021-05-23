package kr.ac.konkuk.planman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.ac.konkuk.planman.databinding.ActivityListViewBinding

class ListViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityListViewBinding
    lateinit var data:ArrayList<MyData>
    lateinit var recyclerView:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initActionBar()
        initData()
        initRecyclerView()
    }

    private fun initActionBar() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return super.onOptionsItemSelected(item)
//    }

    private fun initData() {
        data = ArrayList()
        data.add(MyData("보고서 올리기", "계약 관련 보고서 김과장한테 올려야함", "업무", "noInfo", "noInfo", "noInfo"))
        data.add(MyData("고성호 만나기", "일요일 밤 10시", "약속", "noInfo", "noInfo", "noInfo"))
        data.add(MyData("이마트", "이마트에서 계란 사기", "구매", "noInfo", "noInfo", "noInfo"))
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerView = findViewById<RecyclerView>(R.id.list_recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this@ListViewActivity, LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = ListAdapter(data)
        }
    }
}
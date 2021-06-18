package kr.ac.konkuk.planman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.ac.konkuk.planman.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "설정"

        initSettingMenuList()
    }

    private fun initSettingMenuList() {
        val menuList = ArrayList<SimpleListRecyclerViewAdapter.SimpleListItem>()
        menuList.add(SimpleListRecyclerViewAdapter.SimpleListItem("카테고리 관리", {
            val intent= Intent(this, CategoryListActivity::class.java)
            startActivity(intent)
        }))
        menuList.add(SimpleListRecyclerViewAdapter.SimpleListItem("가져오기/내보내기"))
        menuList.add(SimpleListRecyclerViewAdapter.SimpleListItem("알림 설정"))
        menuList.add(SimpleListRecyclerViewAdapter.SimpleListItem("홈 화면 배경 설정"))

        val listFragment =
            supportFragmentManager.findFragmentById(R.id.setting_menu_list) as SimpleListFragment
        listFragment.setList(menuList)
    }
}
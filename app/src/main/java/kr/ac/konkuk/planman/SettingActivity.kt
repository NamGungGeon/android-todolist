package kr.ac.konkuk.planman

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
            val intent = Intent(this, CategoryListActivity::class.java)
            startActivity(intent)
        }))
        menuList.add(SimpleListRecyclerViewAdapter.SimpleListItem("알림 설정"))
        menuList.add(SimpleListRecyclerViewAdapter.SimpleListItem("초기화", {
            AlertDialog.Builder(this@SettingActivity)
                .setTitle("초기화")
                .setMessage("모든 할 일, 카테고리 정보를 초기화합니다\n계속하시겠습니까?")
                .setPositiveButton("초기화") { dialog, i ->
                    DB(applicationContext).resetDB()
                    dialog.dismiss()
                    Toast.makeText(applicationContext, "초기화 되었습니다", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("닫기") { dialog, i ->
                    dialog.dismiss()
                }.create().show()
        }))

        val listFragment =
            supportFragmentManager.findFragmentById(R.id.setting_menu_list) as SimpleListFragment
        listFragment.setList(menuList)
    }
}
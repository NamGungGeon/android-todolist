package kr.ac.konkuk.planman

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.ac.konkuk.planman.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val todoListVisualizers:ArrayList<Fragment> = ArrayList()

    //임시 변수
    val CATEGORY_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        todoListVisualizers.add(ListTodoFragment())
        todoListVisualizers.add(CalendarTodoFragment())
        todoListVisualizers.add(CalendarTodoFragment())

        binding.apply {
            todoListPager.adapter= object: FragmentStateAdapter(this@MainActivity){
                override fun getItemCount(): Int {
                    return todoListVisualizers.size
                }

                override fun createFragment(position: Int): Fragment {
                    return todoListVisualizers[position]
                }
            }
            todoListPager.offscreenPageLimit= todoListVisualizers.size

            addTodoBtn.setOnClickListener {
                val intent= Intent(applicationContext, AddTodoActivity::class.java)
                intent.putExtra("data", MyData())
                startActivity(intent)
            }

//            //임시 : 실제로는 SettingActivity 나오게끔 바꿀것
//            settingImg.setOnClickListener {
//                val intent = Intent(applicationContext, CategoryListActivity::class.java)
//                startActivityForResult(intent, CATEGORY_REQUEST_CODE)
//            }
        }
    }
}
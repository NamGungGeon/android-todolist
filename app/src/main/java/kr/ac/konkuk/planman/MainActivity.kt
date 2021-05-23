package kr.ac.konkuk.planman

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.ac.konkuk.planman.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val todoListVisualizers:ArrayList<Fragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        todoListVisualizers.add(CalendarTodoFragment())
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
                startActivity(intent)
            }
        }
    }
}
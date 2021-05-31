package kr.ac.konkuk.planman

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.ac.konkuk.planman.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val todoListVisualizers: ArrayList<Fragment> = ArrayList()
    lateinit var drawerToggle: ActionBarDrawerToggle
    var selectedCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        initActionBar()
    }

    private fun initUI() {
        todoListVisualizers.add(ListTodoFragment())
        todoListVisualizers.add(CalendarTodoFragment())
        todoListVisualizers.add(Fragment())

        binding.apply {
            todoListPager.adapter = object : FragmentStateAdapter(this@MainActivity) {
                override fun getItemCount(): Int {
                    return todoListVisualizers.size
                }

                override fun createFragment(position: Int): Fragment {
                    return todoListVisualizers[position]
                }
            }
            todoListPager.offscreenPageLimit = todoListVisualizers.size

            addTodoBtn.setOnClickListener {
                val intent = Intent(applicationContext, AddTodoActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun propagateUpdateCategory(selectedCategory: String) {
        //propagate to all fragments in viewPager
        this.selectedCategory = selectedCategory
    }

    private fun initActionBar() {
        binding.apply {
            drawerToggle = ActionBarDrawerToggle(
                this@MainActivity,
                drawerLayout,
                binding.toolbar,
                R.string.drawer_open,
                R.string.drawer_close
            )
            drawerLayout.addDrawerListener(drawerToggle)
            navView.setNavigationItemSelectedListener {
                // Pass the event to ActionBarDrawerToggle, if it returns
                // true, then it has handled the app icon touch event
                if (drawerToggle.onOptionsItemSelected(it)) {
                    val categoryName = it.title
                    Toast.makeText(this@MainActivity, "${categoryName} 선택됨", Toast.LENGTH_SHORT)
                        .show()
                    drawerLayout.close()
                    true
                }
                // Handle your other action bar items...
                false
            }
            setSupportActionBar(binding.toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
            }
        }
    }

    private fun initCategories(categories: ArrayList<String>) {
        binding.navView.menu.apply {
            clear()
            categories.map {
                val categoryName = it
                val addIcon = layoutInflater.inflate(R.layout.category_add, null, false)
                addIcon.setOnClickListener {
                    //add
                    val intent = Intent(applicationContext, AddTodoActivity::class.java)
                    intent.putExtra("category", categoryName)
                    startActivity(intent)
                }

                add(it).setActionView(addIcon).setOnMenuItemClickListener {
                    //show only todoLists about selected category
                    propagateUpdateCategory(categoryName)
                    false
                }
            }
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        drawerToggle.syncState()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
                val intent = Intent(applicationContext, SettingActivity::class.java)
                startActivity(intent)
            }
            R.id.search -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onSupportNavigateUp(): Boolean {
        binding.drawerLayout.open()
        return super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()

        initCategories(arrayListOf<String>("업무", "약속", "일정", "구매"))
    }

    override fun onBackPressed() {
        val isOpen = binding.drawerLayout.isOpen
        if (isOpen) {
            binding.drawerLayout.close()
        } else {
            super.onBackPressed()
        }
    }
}
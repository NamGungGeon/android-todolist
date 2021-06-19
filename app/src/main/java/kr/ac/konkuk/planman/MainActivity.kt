package kr.ac.konkuk.planman

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import kr.ac.konkuk.planman.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val todoListVisualizers: ArrayList<Fragment> = ArrayList()
    lateinit var drawerToggle: ActionBarDrawerToggle
    private val filterTodoViewModel: FilterTodoViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
//        val db = DB(this)
//        db.resetDB()
//
//        val db2 = DB(this)
//        db2.insertCategory(CategoryData("업무", "보통", "파랑", "보통"))
//        db2.insertCategory(CategoryData("구매", "보통", "노랑", "보통"))
//        db2.insertCategory(
//        CategoryData("약속", "보통", "빨강", "보통"))

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (LocationManager.getInstance().requirePermission(this, 100)) {
            LocationNotificationService.start(this)
        }

        initUI()
        initActionBar()
    }

    private fun initUI() {
        todoListVisualizers.add(ListTodoFragment())
        todoListVisualizers.add(CalendarTodoFragment())
        todoListVisualizers.add(MapTodo(this).mapFragment)

        binding.apply {
            todoListPager.adapter = object : FragmentStateAdapter(this@MainActivity) {
                override fun getItemCount(): Int {
                    return todoListVisualizers.size
                }

                override fun createFragment(position: Int): Fragment {
                    return todoListVisualizers[position]
                }
            }
            todoListPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    onViewModeChanged(position)
                }
            })
            todoListPager.offscreenPageLimit = todoListVisualizers.size

            addTodoBtn.setOnClickListener {
                val intent = Intent(applicationContext, AddTodoActivity::class.java)
                intent.putExtra("data", MyData2())
                startActivity(intent)
            }

            listViewMode.setOnClickListener {
                todoListPager.setCurrentItem(0, true)
            }
            calendarViewMode.setOnClickListener {
                todoListPager.setCurrentItem(1, true)
            }
            mapViewMode.setOnClickListener {
                todoListPager.setCurrentItem(2, true)
            }

            searchTodoInput.addTextChangedListener {
                filterTodoViewModel.setSearchKeyword(it.toString())
            }
            searchTodoInput.visibility= View.GONE

//            //임시 : 실제로는 SettingActivity 나오게끔 바꿀것
//            settingImg.setOnClickListener {
//                val intent = Intent(applicationContext, CategoryListActivity::class.java)
//                startActivityForResult(intent, CATEGORY_REQUEST_CODE)
//            }
        }
    }

    private fun propagateUpdateCategory(selectedCategory: String) {
        //propagate to all fragments in viewPager
        var value: String? = selectedCategory
        if (selectedCategory == "전체") {
            value = null
            supportActionBar?.title = "전체 할 일"
            Toast.makeText(this, "전체 할 일을 표시합니다", Toast.LENGTH_SHORT).show()
        } else {
            supportActionBar?.title = selectedCategory
            Toast.makeText(this, "${selectedCategory} 카테고리의 할 일을 표시합니다", Toast.LENGTH_SHORT).show()
        }
        filterTodoViewModel.setSelectedCategory(value)
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
                title = "전체 할 일"
            }
        }
    }

    private fun initCategories(categories: ArrayList<String>) {
        binding.categoryAddBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, CategoryAddActivity::class.java)
            startActivity(intent)
        }
        binding.navView.menu.apply {
            clear()
            val realCategories = ArrayList(categories)
            realCategories.add(0, "전체")
            realCategories.map {
                val categoryName = it
                val addIcon = layoutInflater.inflate(R.layout.layout_category_add, null, false)
                addIcon.setOnClickListener {
                    //add
                    val intent = Intent(applicationContext, AddTodoActivity::class.java)
                    intent.putExtra("data", MyData2())
                    intent.putExtra("type", categoryName)
                    startActivity(intent)
                }
                add(it).setOnMenuItemClickListener {
                    //show only todoLists about selected category
                    propagateUpdateCategory(categoryName)
                    binding.drawerLayout.close()
                    false
                }.apply {
                    if (it != "전체")
                        this.actionView = addIcon
                }
            }
        }
    }

    private fun onViewModeChanged(position: Int) {
        binding.apply {
            listViewMode.setColorFilter(resources.getColor(R.color.light_gray))
            calendarViewMode.setColorFilter(resources.getColor(R.color.light_gray))
            mapViewMode.setColorFilter(resources.getColor(R.color.light_gray))
            when (position) {
                0 -> listViewMode.setColorFilter(resources.getColor(R.color.black))
                1 -> calendarViewMode.setColorFilter(resources.getColor(R.color.black))
                2 -> mapViewMode.setColorFilter(resources.getColor(R.color.black))
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
                binding.searchTodoInput.apply {
                    if (visibility == View.GONE) {
                        visibility = View.VISIBLE
                        //open soft keyboard
                        requestFocus()
                        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    } else {
                        visibility = View.GONE
                    }
                    text.clear()
                }
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

//        initCategories(arrayListOf("업무", "구매", "약속"))
        initCategories(
            ArrayList(
                DB(this).readCategory().map { if (it.type == null) "" else it.type!! }.toList()
                    .filter { it != "" })
        )
    }

    override fun onBackPressed() {
        val isOpen = binding.drawerLayout.isOpen
        if (isOpen) {
            binding.drawerLayout.close()
        } else {
            if(binding.searchTodoInput.visibility== View.VISIBLE){
                binding.searchTodoInput.visibility= View.GONE
                binding.searchTodoInput.text.clear()
            }else{
                super.onBackPressed()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (LocationManager.getInstance().checkRequiredPermission(this)) {
            LocationNotificationService.start(this)
        }
    }
}
package kr.ac.konkuk.planman

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.ac.konkuk.planman.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val todoListVisualizers: ArrayList<Fragment> = ArrayList()
    lateinit var drawerToggle: ActionBarDrawerToggle

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
        todoListVisualizers.add(CalendarTodoFragment())

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
    override fun onBackPressed() {
        val isOpen= binding.drawerLayout.isOpen
        if(isOpen){
            binding.drawerLayout.close()
        }else{
            super.onBackPressed()
        }
    }
}
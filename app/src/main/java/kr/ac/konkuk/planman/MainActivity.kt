package kr.ac.konkuk.planman

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.navigation.NavigationView
import kr.ac.konkuk.planman.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMainBinding
    val todoListVisualizers: ArrayList<Fragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

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

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController =NavController(this)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navitivation_test, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_navitivation_test)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
package kr.ac.konkuk.planman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()


        Thread{
            Thread.sleep(1500)

            if(!isDestroyed){
                runOnUiThread{
                    val intent= Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }.start()
    }
}
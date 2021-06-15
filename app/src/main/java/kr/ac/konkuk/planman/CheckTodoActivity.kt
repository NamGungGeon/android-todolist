package kr.ac.konkuk.planman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import kr.ac.konkuk.planman.databinding.ActivityCheckTodoBinding

class CheckTodoActivity : AppCompatActivity() {

    lateinit var binding: ActivityCheckTodoBinding
    lateinit var data: MyData2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = intent.getSerializableExtra("data") as MyData2
        init()
    }

    private fun initText(textView: TextView, string: String?) {
        if(string != null)
            textView.text = string
        else
            textView.isVisible = false
    }

    private fun init() {
        Toast.makeText(this, "check", Toast.LENGTH_LONG).show()
        initText(binding.title, data.title)
        initText(binding.checkTodoContent, data.content)

        binding.checkWebAddress.checkTodoCategoryIcon.setImageResource(R.drawable.ic_baseline_find_in_page_24)
        binding.checkWebAddress.checkTodoCategoryTitle.text = "웹사이트"
        initText(binding.checkWebAddress.checkTodoCategoryContent, data.attachment.webSite)

        binding.checkPhoneNumber.checkTodoCategoryTitle.text = "전화번호"
        initText(binding.checkPhoneNumber.checkTodoCategoryContent, data.attachment.phoneNumber)

        binding.checkDateTime.checkTodoCategoryIcon.setImageResource(R.drawable.ic_baseline_edit_calendar_24)
        binding.checkDateTime.checkTodoCategoryContent.text = "날짜/시간"
        initText(binding.checkDateTime.checkTodoCategoryContent, data.notification.notifyDateTime.toString())

        binding.checkRadius.checkTodoCategoryIcon.setImageResource(R.drawable.ic_baseline_place_24)
        binding.checkRadius.checkTodoCategoryContent.text = "거리 반경"
        initText(binding.checkRadius.checkTodoCategoryContent, "tmp")

        binding.editButton.setOnClickListener {
            val intent= Intent(applicationContext, AddTodoActivity::class.java)
            intent.putExtra("data", data)
            startActivity(intent)
        }
    }



}
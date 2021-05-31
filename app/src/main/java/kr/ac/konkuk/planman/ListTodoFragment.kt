package kr.ac.konkuk.planman

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDateTime

class ListTodoFragment : Fragment() {
    var data: ArrayList<MyData> = ArrayList()
    lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentListTodoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentListTodoBinding.bind(view)

        initData()
        initRecyclerView()
    }

    private fun initData() {
        data = ArrayList()
        data.add(MyData("보고서 올리기", "계약 관련 보고서 김과장한테 올려야함", "업무", "noInfo", "noInfo", "noInfo", LocalDateTime.now(), "noInfo"))
        data.add(MyData("고성호 만나기", "일요일 밤 10시", "약속", "noInfo", "noInfo", "noInfo", LocalDateTime.now(), "noInfo"))
        data.add(MyData("이마트", "이마트에서 계란 사기", "구매", "noInfo", "noInfo", "noInfo", LocalDateTime.now(), "noInfo"))
    }

    private fun initRecyclerView() {
        recyclerView = view!!.findViewById<RecyclerView>(R.id.list_recyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = ListAdapter(data)
    }
}
package kr.ac.konkuk.planman

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SimpleListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_simple_list, container, false)
    }

    fun setList(list: ArrayList<SimpleListRecyclerViewAdapter.SimpleListItem>) {
        if (view != null && view is RecyclerView) {
            (view as RecyclerView).apply {
                layoutManager = LinearLayoutManager(context)
                adapter = SimpleListRecyclerViewAdapter(list)
            }
        }
    }
}
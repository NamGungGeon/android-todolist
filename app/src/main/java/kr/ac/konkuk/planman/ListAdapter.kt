package kr.ac.konkuk.planman

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(val items:ArrayList<MyData>) : RecyclerView.Adapter<ListAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView : TextView= itemView.findViewById(R.id.todoListText)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
        holder.itemTitleTextView.text = items[position].title

        val titleType = items[position].type
        if (titleType == "업무") {
            holder.itemTitleTextView.setBackgroundResource(R.drawable.list_edge_work)
        } else if (titleType == "약속") {
            holder.itemTitleTextView.setBackgroundResource(R.drawable.list_edge_appointment)
        } else if (titleType == "구매") {
            holder.itemTitleTextView.setBackgroundResource(R.drawable.list_edge_purchase)
        }

    }


}
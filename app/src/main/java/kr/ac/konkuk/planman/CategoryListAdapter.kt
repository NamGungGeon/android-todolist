package kr.ac.konkuk.planman

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryListAdapter(val items:ArrayList<CategoryData>) : RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(holder: ViewHolder, view:View, data:CategoryData, position: Int)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    var itemClickListener:OnItemClickListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeText : TextView = itemView.findViewById(R.id.todoListText)
        init {
            itemView.setOnClickListener {
                itemClickListener?.OnItemClick(this, it, items[adapterPosition], adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CategoryListAdapter.ViewHolder, position: Int) {
        holder.typeText.text = items[position].type

        holder.apply { 
            typeText.text = items[position].type

            //textSize select : 크게, 보통, 작게
            if (items[position].textSize == "크게") {
                typeText.textSize = 24f
            } else if (items[position].textSize == "보통") {
                typeText.textSize = 18f
            } else {    //작게
                typeText.textSize = 14f
            }

            //textColor select : 파랑, 노랑, 빨강, 검정
            if (items[position].textColor == "파랑") {
                typeText.setTextColor(Color.parseColor("#287FB9"))
                typeText.setBackgroundResource(R.drawable.list_edge_work)
            } else if (items[position].textColor == "빨강") {
                typeText.setTextColor(Color.parseColor("#D35415"))
                typeText.setBackgroundResource(R.drawable.list_edge_purchase)
            } else if (items[position].textColor == "노랑") {
                typeText.setTextColor(Color.parseColor("#F39D19"))
                typeText.setBackgroundResource(R.drawable.list_edge_appointment)
            } else {    //검정
                typeText.setTextColor(Color.BLACK)
                typeText.setBackgroundResource(R.drawable.list_edge_black)
            }

            //textStyle select : 진하게, 보통, 이탤릭체
            if (items[position].textStyle == "진하게") {
                typeText.setTypeface(typeText.typeface, Typeface.BOLD)
            } else if (items[position].textStyle == "보통") {
                typeText.setTypeface(typeText.typeface, Typeface.NORMAL)
            } else {    //이탤릭체
                typeText.setTypeface(typeText.typeface, Typeface.ITALIC)
            }
        }
    }



}
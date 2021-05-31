package kr.ac.konkuk.planman

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryModifyAdapter(val items:ArrayList<CategoryData>) : RecyclerView.Adapter<CategoryModifyAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeText : TextView = itemView.findViewById(R.id.todoListText)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryModifyAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CategoryModifyAdapter.ViewHolder, position: Int) {
        holder.typeText.text = items[position].type

        holder.apply {
            typeText.text = items[position].type

            //textSize select
            if (items[position].textSize == "크게") { //크게, 보통, 작게 : if문으로 설정
                typeText.textSize = 24.0f
            } else if (items[position].textSize == "보통") {
                typeText.textSize = 18.0f
            } else {    //작게
                typeText.textSize = 14.0f
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
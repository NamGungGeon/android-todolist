package kr.ac.konkuk.planman

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(val items:ArrayList<MyData2>) : RecyclerView.Adapter<ListAdapter.ViewHolder>(){

    lateinit var db: DB

    interface OnItemClickListener{
        fun OnItemClick(holder: ViewHolder, view:View, data:MyData2, position: Int)
    }

    var itemClickListener:OnItemClickListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView : TextView= itemView.findViewById(R.id.todoListTextRow)

        init {
            itemView.setOnClickListener {
                itemClickListener?.OnItemClick(this, it, items[adapterPosition], adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        db = DB(parent.context)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
        holder.titleTextView.text = items[position].title

        val titleType = items[position].type

        val categoryDataList = db.readCategory()

        var isIn : Boolean = false

        if (titleType == null) {
            holder.titleTextView.setBackgroundResource(R.drawable.list_edge_default)
        } else {
            for (i in categoryDataList) {
                if (titleType == i.type.toString()) {
                    isIn = true

                    if (i.textSize == "크게")
                        holder.titleTextView.textSize = 24f
                    else if (i.textSize == "보통")
                        holder.titleTextView.textSize = 18f
                    else    //작게
                        holder.titleTextView.textSize = 14f


                    if (i.textStyle == "보통")
                        holder.titleTextView.setTypeface(
                            holder.titleTextView.typeface,
                            Typeface.NORMAL
                        )
                    else if (i.textStyle == "진하게")
                        holder.titleTextView.setTypeface(
                            holder.titleTextView.typeface,
                            Typeface.BOLD
                        )
                    else
                        holder.titleTextView.setTypeface(
                            holder.titleTextView.typeface,
                            Typeface.ITALIC
                        )


                    if (i.textColor == "파랑") {
                        holder.titleTextView.setTextColor(Color.parseColor("#287FB9"))
                        holder.titleTextView.setBackgroundResource(R.drawable.list_edge_work)
                    } else if (i.textColor == "노랑") {
                        holder.titleTextView.setTextColor(Color.parseColor("#F39D19"))
                        holder.titleTextView.setBackgroundResource(R.drawable.list_edge_appointment)
                    } else if (i.textColor == "빨강") {
                        holder.titleTextView.setTextColor(Color.parseColor("#D35415"))
                        holder.titleTextView.setBackgroundResource(R.drawable.list_edge_purchase)
                    } else {    //검정
                        holder.titleTextView.setTextColor(Color.BLACK)
                        holder.titleTextView.setBackgroundResource(R.drawable.list_edge_black)
                    }
                }

            }
        }



        if (!isIn) {
            holder.titleTextView.setBackgroundResource(R.drawable.list_edge_default)
        }
    }
}
package kr.ac.konkuk.planman

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import kr.ac.konkuk.planman.databinding.FragmentItemBinding

class SimpleListRecyclerViewAdapter(
    private val values: List<SimpleListItem>
) : RecyclerView.Adapter<SimpleListRecyclerViewAdapter.ViewHolder>() {
    class SimpleListItem(val label: String, val onClick: (() -> Unit)? = null)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.labelView.text = item.label
        holder.itemView.setOnClickListener {
            item.onClick?.run {
                this()
            }
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val labelView: TextView = binding.label

        override fun toString(): String {
            return super.toString() + " '" + labelView.text + "'"
        }
    }

}
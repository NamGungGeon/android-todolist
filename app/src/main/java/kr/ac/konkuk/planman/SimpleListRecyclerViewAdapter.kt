package kr.ac.konkuk.planman

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.konkuk.planman.databinding.ListSimpleItemBinding

class SimpleListRecyclerViewAdapter(
    private val values: List<SimpleListItem>
) : RecyclerView.Adapter<SimpleListRecyclerViewAdapter.ViewHolder>() {
    class SimpleListItem(
        val label: String,
        val onClick: (() -> Unit)? = null,
        val onBindViewHolder: ((holder: ViewHolder) -> Unit)? = null
    ) {
        class Builder() {
            val list = ArrayList<SimpleListItem>()
            fun append(
                label: String,
                onClick: (() -> Unit)? = null,
                onBindViewHolder: ((holder: ViewHolder) -> Unit)? = null
            ): Builder {
                list.add(SimpleListItem(label, onClick, onBindViewHolder))
                return this
            }

            fun build(): ArrayList<SimpleListItem> {
                return list
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListSimpleItemBinding.inflate(
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

        item.onBindViewHolder?.run {
            this(holder)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: ListSimpleItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val labelView: TextView = binding.label

        override fun toString(): String {
            return super.toString() + " '" + labelView.text + "'"
        }
    }

}
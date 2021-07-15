package com.example.deadspace.ui.deadlines.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.deadspace.R
import com.example.deadspace.data.database.MyDeadlinesData

//TODO : use paging
class DeadlinesListAdapter(val viewModel: DeadlineViewModel) :
    RecyclerView.Adapter<DeadlinesListAdapter.MyViewHolder>() {

    private var items : List<MyDeadlinesData> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.deadline_list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.deadlineTitleTextView?.text = item.title
        holder.deadlineDisciplineTextView?.text = item.discipline
        holder.deadlineDateTextView?.text = item.lastDate

        holder.deleteButton?.setOnClickListener {
            viewModel.onDeleteDeadline(
                holder.deadlineTitleTextView?.text.toString(),
                holder.deadlineDisciplineTextView?.text.toString(),
                holder.deadlineDateTextView?.text.toString()
            )
        }
    }

    fun updateItems(items: List<MyDeadlinesData>) {
        this.items = items
        notifyDataSetChanged()
    }

    //TODO : use binding
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //binding =

        var deadlineTitleTextView: TextView? = null
        var deadlineDisciplineTextView: TextView? = null
        var deadlineDateTextView: TextView? = null

        var deleteButton: ImageButton? = null

        init {

            deadlineTitleTextView = itemView.findViewById(R.id.deadline_item_title)
            deadlineDisciplineTextView = itemView.findViewById(R.id.deadline_item_discipline)
            deadlineDateTextView = itemView.findViewById(R.id.deadline_item_date)

            deleteButton = itemView.findViewById(R.id.deadline_item_delete_button)

        }
    }

    override fun getItemCount() = items.size

}
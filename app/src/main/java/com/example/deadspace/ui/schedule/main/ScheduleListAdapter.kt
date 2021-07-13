package com.example.deadspace.ui.schedule.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.deadspace.R
import com.example.deadspace.data.schedule.MyPairData

//TODO : use paging
class ScheduleListAdapter(val viewModel: ScheduleViewModel) :
    RecyclerView.Adapter<ScheduleListAdapter.MyViewHolder>() {

    private var items : List<MyPairData> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.schedule_list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.scheduleNameTextView?.text = item.name
        holder.scheduleTypeTextView?.text = item.type
        holder.scheduleNumberTextView?.text = item.time
        holder.scheduleTeachersTextView?.text = item.teachers
        holder.scheduleAuditoriumTextView?.text = item.address
    }

    fun updateItems(items: List<MyPairData>) {
        this.items = items
        notifyDataSetChanged()
    }

    //TODO : use binding
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //binding =

        var scheduleNameTextView: TextView? = null
        var scheduleTypeTextView: TextView? = null
        var scheduleNumberTextView: TextView? = null
        var scheduleTeachersTextView: TextView? = null
        var scheduleAuditoriumTextView: TextView? = null

        init {

            scheduleNameTextView = itemView.findViewById(R.id.schedule_item_name)
            scheduleNumberTextView = itemView.findViewById(R.id.schedule_item_number)
            scheduleTypeTextView = itemView.findViewById(R.id.schedule_item_type)
            scheduleTeachersTextView = itemView.findViewById(R.id.schedule_item_teachers)
            scheduleAuditoriumTextView = itemView.findViewById(R.id.schedule_item_auditorium)
        }
    }

    override fun getItemCount() = items.size

}
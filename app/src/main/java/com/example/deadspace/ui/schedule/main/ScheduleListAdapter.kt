package com.example.deadspace.ui.schedule.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.deadspace.DeadSpace
import com.example.deadspace.R
import com.example.deadspace.data.database.PairData

class ScheduleListAdapter(private val pairClickListener: ScheduleActivity.PairClickListener) :
    RecyclerView.Adapter<ScheduleListAdapter.MyViewHolder>() {

    private var items : List<PairData> = listOf()
    private val res = DeadSpace.appContext.resources

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.schedule_list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]

        holder.scheduleNameTextView?.text = item.Disc
        holder.scheduleTypeTextView?.text = item.Type

        holder.scheduleNumberTextView?.text =
            if (item.Less != 0)  item.Less.toString()
        else
            "-"

        holder.scheduleBuildingTextView?.text = item.Build
        holder.scheduleAuditoriumTextView?.text = item.Rooms

        holder.scheduleTimeTextView?.text = res.getString(R.string.pair_time_counter,
            item.StartTime,  item.EndTime)

        holder.deleteButton?.setOnClickListener {
            pairClickListener.onClickDeletePair(item)
        }

        holder.itemView.setOnClickListener{
            val pos = holder.adapterPosition
            if(pos != DiffUtil.DiffResult.NO_POSITION) {
                //todo itemClickListener
                pairClickListener.onClickPairItem(item)
            }
        }

    }

    fun updateItems(items: List<PairData>) {
        this.items = items
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var scheduleNameTextView: TextView? = null
        var scheduleTypeTextView: TextView? = null
        var scheduleNumberTextView: TextView? = null
        var scheduleBuildingTextView: TextView? = null
        var scheduleAuditoriumTextView: TextView? = null
        var scheduleTimeTextView: TextView? = null

        var deleteButton: ImageButton? = null

        init {

            scheduleNameTextView = itemView.findViewById(R.id.schedule_item_name)
            scheduleNumberTextView = itemView.findViewById(R.id.schedule_item_number)
            scheduleTypeTextView = itemView.findViewById(R.id.schedule_item_type)
            scheduleBuildingTextView = itemView.findViewById(R.id.schedule_item_building)
            scheduleAuditoriumTextView = itemView.findViewById(R.id.schedule_item_auditorium)
            scheduleTimeTextView = itemView.findViewById(R.id.schedule_item_time)

            deleteButton = itemView.findViewById(R.id.schedule_item_delete_button)
        }
    }

    override fun getItemCount() = items.size

}
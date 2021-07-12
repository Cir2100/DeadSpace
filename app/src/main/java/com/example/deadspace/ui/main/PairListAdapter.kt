package com.example.deadspace.view.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.deadspace.R
import com.example.deadspace.data.schedule.MyPairData

//TODO : use paging
class PairListAdapter(val viewModel: StartViewModel) :
    RecyclerView.Adapter<PairListAdapter.MyViewHolder>() {

    private var items : List<MyPairData> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.pair_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.pairNameTextView?.text = item.name
        holder.pairTypeTextView?.text = item.type
        holder.pairTimeTextView?.text = item.time
        holder.pairWeekTextView?.text = item.week.toString()
        holder.pairGroupsTextView?.text = item.groups
        holder.pairTeachersTextView?.text = item.teachers
        holder.pairAddressTextView?.text = item.address
    }

    fun updateItems(items: List<MyPairData>) {
        this.items = items
        notifyDataSetChanged()
    }

    //TODO : use binding
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var pairNameTextView: TextView? = null
        var pairTypeTextView: TextView? = null
        var pairTimeTextView: TextView? = null
        var pairWeekTextView: TextView? = null
        var pairGroupsTextView: TextView? = null
        var pairTeachersTextView: TextView? = null
        var pairAddressTextView: TextView? = null

        init {
            pairNameTextView = itemView.findViewById(R.id.pair_item_name)
            pairTypeTextView = itemView.findViewById(R.id.pair_item_type)
            pairTimeTextView = itemView.findViewById(R.id.pair_item_time)
            pairWeekTextView = itemView.findViewById(R.id.pair_item_week)
            pairGroupsTextView = itemView.findViewById(R.id.pair_item_groups)
            pairTeachersTextView = itemView.findViewById(R.id.pair_item_teachers)
            pairAddressTextView = itemView.findViewById(R.id.pair_item_address)
        }
    }

    override fun getItemCount() = items.size

}
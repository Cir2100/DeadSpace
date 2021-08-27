package com.example.deadspace.ui.exams

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.deadspace.data.database.ExamData
import com.example.deadspace.databinding.ExamListItemBinding


class ExamListAdapter()
    : RecyclerView.Adapter<ExamListAdapter.ViewHolder>(){

    private var items : List<ExamData> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ExamListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.dateTextView.text = item.Date
        holder.changeTextView.text = item.Change
        holder.discTextView.text = item.Disc
        holder.buildTextView.text = item.Build
        holder.auditoriumTextView.text = item.Rooms
        holder.teachersTextView.text = item.TeachersText

    }

    fun updateItems(items: List<ExamData>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(binding: ExamListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val dateTextView: TextView = binding.examItemDate
        val changeTextView: TextView = binding.examItemChange
        val discTextView: TextView = binding.examItemDisc
        val buildTextView: TextView = binding.examItemBuilding
        val auditoriumTextView: TextView = binding.examItemAuditorium
        val teachersTextView: TextView = binding.examItemTeachers
    }

}
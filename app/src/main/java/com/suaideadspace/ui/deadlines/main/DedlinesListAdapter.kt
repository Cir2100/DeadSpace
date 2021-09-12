package com.suaideadspace.ui.deadlines.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.suaideadspace.DeadSpace
import com.suaideadspace.R
import com.suaideadspace.data.database.MyDeadlinesData
import java.text.SimpleDateFormat
import java.util.*

class DeadlinesListAdapter(val viewModel: DeadlineViewModel) :
    RecyclerView.Adapter<DeadlinesListAdapter.MyViewHolder>() {

    private var items : List<MyDeadlinesData> = listOf()

    private val context = DeadSpace.appContext
    private val resources = context.resources

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

        //Current date
        val date = Calendar.getInstance()
        val formatter = SimpleDateFormat("dd MMMM")
        date.set(Calendar.MONTH, item.lastDate.substring(3,5).toInt() - 1)
        date.set(Calendar.DATE, item.lastDate.substring(0,2).toInt())

        holder.deadlineDateTextView?.text = formatter.format(date.time)

        if (item.isDone)
            holder.doneButton?.setImageDrawable(resources.getDrawable(R.drawable.done_deadline, context.theme))
        else
            holder.doneButton?.setImageDrawable(resources.getDrawable(R.drawable.undone_deadline, context.theme))

        holder.deleteButton?.setOnClickListener {
            viewModel.onDeleteDeadline(item.id)
        }

        holder.doneButton?.setOnClickListener {
            viewModel.onDoneChange(item.id)
        }
    }

    fun updateItems(items: List<MyDeadlinesData>) {
        this.items = items
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //binding =

        var deadlineTitleTextView: TextView? = null
        var deadlineDisciplineTextView: TextView? = null
        var deadlineDateTextView: TextView? = null

        var deleteButton: ImageButton? = null

        var doneButton : ImageButton? = null

        init {

            deadlineTitleTextView = itemView.findViewById(R.id.deadline_item_title)
            deadlineDisciplineTextView = itemView.findViewById(R.id.deadline_item_discipline)
            deadlineDateTextView = itemView.findViewById(R.id.deadline_item_date)

            deleteButton = itemView.findViewById(R.id.deadline_item_delete_button)

            doneButton = itemView.findViewById(R.id.done_deadline_button)

        }
    }

    override fun getItemCount() = items.size

}
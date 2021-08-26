package com.example.deadspace.ui.schedule.main

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import android.R




class ScheduleDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val type = requireArguments().getBoolean("type")

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(if (type) resources.getString(com.example.deadspace.R.string.delete_users_schedule)
                    else resources.getString(com.example.deadspace.R.string.delete_pair))
                .setCancelable(true)
                .setPositiveButton("Да") { _, _ ->
                    if (type) (activity as ScheduleActivity).okClickedDeleteUsersSchedule()
                    else (activity as ScheduleActivity).okClickedDeletePair()
                }
                .setNegativeButton("Нет") { _, _ -> }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
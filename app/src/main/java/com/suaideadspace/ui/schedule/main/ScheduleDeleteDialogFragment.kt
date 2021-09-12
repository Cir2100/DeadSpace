package com.suaideadspace.ui.schedule.main

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment




class ScheduleDeleteDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val type = requireArguments().getBoolean("type")

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(if (type) resources.getString(com.suaideadspace.R.string.delete_users_schedule)
                    else resources.getString(com.suaideadspace.R.string.delete_pair))
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
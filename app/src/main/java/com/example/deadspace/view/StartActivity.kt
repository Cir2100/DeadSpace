package com.example.deadspace.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.deadspace.R
import com.example.deadspace.schedule.SheaduleLodaer
import com.example.deadspace.view.ui.main.StartFragment

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_activity)
        //val binding : ViewDataBinding = DataBindingUtil.setContentView(this, R.layout.start_activity)
        //TODO: use normal
        SheaduleLodaer().loadSheadule("1942", true)
    }
}
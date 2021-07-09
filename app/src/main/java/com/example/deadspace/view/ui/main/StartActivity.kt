package com.example.deadspace.view.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.deadspace.R
import com.example.deadspace.databinding.StartActivityBinding

class StartActivity : AppCompatActivity() {

    private lateinit var viewModel  : StartViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(StartViewModel::class.java)

        val binding : StartActivityBinding = DataBindingUtil.setContentView(this, R.layout.start_activity)
        binding.startViewModel = viewModel
        binding.lifecycleOwner = this


        viewModel.data.observe(this) { value ->
            value?.let {
                binding.message.text = it
            }
        }

    }
}
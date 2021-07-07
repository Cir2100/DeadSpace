package com.example.deadspace.view.ui.main

<<<<<<< HEAD
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
=======
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
>>>>>>> origin/design
import com.example.deadspace.R

class StartFragment : Fragment() {

    companion object {
        fun newInstance() = StartFragment()
    }

    private lateinit var viewModel: StartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
<<<<<<< HEAD
        return inflater.inflate(R.layout.main_fragment, container, false)
=======
        return inflater.inflate(R.layout.start_fragment, container, false)
>>>>>>> origin/design
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(StartViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
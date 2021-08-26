package com.example.deadspace.ui.schedule.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.deadspace.databinding.PairInfoFragmentBinding


class PairDialogFragment  : DialogFragment() {

    private var _binding : PairInfoFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PairInfoFragmentBinding.inflate(inflater, container, false)

        binding.pairInfoLess.text = requireArguments().getInt("less").toString()
        binding.pairInfoTime.text = requireArguments().getString("time")
        binding.pairInfoType.text = requireArguments().getString("type")
        binding.pairInfoDisc.text = requireArguments().getString("disc")
        binding.pairInfoAuditorium.text = requireArguments().getString("auditorium")
        binding.pairInfoBuilding.text = requireArguments().getString("build")
        binding.pairInfoGroups.text = requireArguments().getString("groups")
        binding.pairInfoTeachers.text = requireArguments().getString("teachers")

        return binding.root
    }
}
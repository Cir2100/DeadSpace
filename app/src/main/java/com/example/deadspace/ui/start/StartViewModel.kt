package com.example.deadspace.ui.start

import androidx.lifecycle.*
import com.example.deadspace.data.database.MyPairDAO
import com.example.deadspace.data.schedule.ScheduleEditor
import com.example.deadspace.data.schedule.ScheduleLoader
import com.example.deadspace.ui.singleArgViewModelFactory

//TODO: use Hilt
//TODO : use FLOW?
//@HiltViewModel
class StartViewModel(private val myPairDAO: MyPairDAO) : ViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::StartViewModel)
    }

    //TODO: this in constructor
    private val scheduleLoader = ScheduleLoader(myPairDAO)
    private val scheduleEditor = ScheduleEditor(myPairDAO)


    /*private val _data = MutableLiveData<String>()
    val data : LiveData<String> = _data*/

    //val data = scheduleLoader.pairs

    val myPairList = scheduleLoader.pairs


    private val _isVisibleList = MutableLiveData<Boolean>()
    val isVisibleList : LiveData<Boolean> = _isVisibleList
//android:visibility="@{startViewModel.myPairList.size != 0}"

    var nameGroupListener : String = "1942"
    private var isUsers = false

    //TODO : use anti
    fun onChangeIsUser() {
        isUsers = !isUsers
    }

    fun onDelete() {

    }



}
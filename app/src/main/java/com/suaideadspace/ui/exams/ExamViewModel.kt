package com.suaideadspace.ui.exams

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suaideadspace.DeadSpace
import com.suaideadspace.data.database.GroupAndTeacherData
import com.suaideadspace.data.database.getDatabase
import com.suaideadspace.data.exam.getExamLoaderExamRepo
import kotlinx.coroutines.launch
import java.io.IOException

class ExamViewModel : ViewModel() {

    private val examRepo = getExamLoaderExamRepo()

    val listExams = examRepo.listExams

    var querySuggestions : List<GroupAndTeacherData> = listOf()

    var currentGroup : String = ""

    private val _spinner = MutableLiveData(false)
    val spinner: LiveData<Boolean>
        get() = _spinner

    private val _toast : MutableLiveData<String?> = examRepo.error
    val toast: LiveData<String?>
        get() = _toast

    fun onToastShown() {
        _toast.value = null
    }

    fun onSearch(groupName : String) {

        viewModelScope.launch {
            try {
                _spinner.value = true
                currentGroup = groupName
                examRepo.updateExams(groupName)
            } catch (e: IOException) {
                _spinner.value = false
                Log.e(this.javaClass.simpleName, e.message.toString())
                _toast.value = e.message.toString()
            } catch (e: Exception) {
                _spinner.value = false
                Log.e(this.javaClass.simpleName, e.message.toString())
                _toast.value = e.message.toString()
            } finally {
                _spinner.value = false
            }
        }
    }

    init {
        updateGroupAndTeacher()
    }

    private fun updateGroupAndTeacher() {
        viewModelScope.launch {
            examRepo.updateGroupAndTeacher()
            querySuggestions = getDatabase(DeadSpace.appContext).myGroupAndTeacherDAO.getAllExams()
        }
    }

}
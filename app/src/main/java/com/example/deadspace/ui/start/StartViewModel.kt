package com.example.deadspace.ui.start

import android.util.Log
import androidx.lifecycle.*
import com.example.deadspace.data.suai.*
import kotlinx.coroutines.launch

//TODO: use Hilt
//TODO : use FLOW?
//@HiltViewModel
class StartViewModel : ViewModel() {

    //val SUAILoader : Loader = Loader.INSTANCE

    fun getHTTP() {
        viewModelScope.launch {


            Log.e(this.javaClass.simpleName, Loader.getSchedule("1942").toString())
            //val result = Loader.getInstance()
            // Display result of the network request to the user
            /*when (result) {
                //is Result.Success<SemInfo> -> Log.e(this.javaClass.simpleName, result.data.toString())
                is Result.Success<List<Pair>> -> Log.e(this.javaClass.simpleName, result.data.toString())
                is Result.Error -> Log.e(this.javaClass.simpleName, result.exception.message!!) // TODO : Show error in UI
            }*/

        }
    }

}
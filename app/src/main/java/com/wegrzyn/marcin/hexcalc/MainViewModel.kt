package com.wegrzyn.marcin.hexcalc


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel : ViewModel() {

    var hexString: LiveData<String>
    var decString: LiveData<String>
    var binString: LiveData<String>
    var typeSelected: LiveData<TypeNumber>

    init {
        hexString = Repo.hexString
        decString = Repo.decString
        binString = Repo.binString
        typeSelected = Repo.typeSelected
    }

    fun test(){
            viewModelScope.launch {
                repeat(10){
                    delay(100L)
                    Log.d(Repo.TAG,"repeat $it")
                    doWorld()
                }
            }
            Log.d(Repo.TAG,"end")

    }
    suspend fun doWorld() {
        delay(1000L)
        Log.d(Repo.TAG,"repeat ")
    }

}
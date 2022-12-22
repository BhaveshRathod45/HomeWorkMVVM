package com.imaginato.homeworkmvvm.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imaginato.homeworkmvvm.exts.LOG_TYPE_INFO
import com.imaginato.homeworkmvvm.exts.printLog
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@KoinApiExtension
abstract class BaseViewModel : ViewModel(), KoinComponent {
    protected var _progress: MutableLiveData<Boolean> = MutableLiveData()
    protected var _errorMessage: MutableLiveData<String> = MutableLiveData()

    val errorMessage: LiveData<String>
        get() {
            return _errorMessage
        }
    val progress: LiveData<Boolean>
        get() {
            return _progress
        }

    init {
        javaClass.simpleName.printLog(LOG_TYPE_INFO, "created")
    }

    override fun onCleared() {
        super.onCleared()
        _progress.value=false
        javaClass.simpleName.printLog(LOG_TYPE_INFO, "destroyed")
    }
}
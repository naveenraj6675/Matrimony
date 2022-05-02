package com.naveen.matrimony.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.gotech.youtube.enums.LoaderStatus
import com.naveen.matrimony.repositories.UserDataRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


open class MyBaseViewModel(application: Application) : AndroidViewModel(application),
    CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + rootJob

    protected var errorLiveData = MutableLiveData<String?>()

    // ...because this is what we'll want to expose
    val errorMediatorLiveData = MediatorLiveData<String?>()


    val userDataRepository: UserDataRepository by lazy {
        UserDataRepository.getInstance(application)
    }


    var isLoading = MutableLiveData<LoaderStatus>()

    val rootJob = Job()


    protected val handler = CoroutineExceptionHandler { _, exception ->
        isLoading.postValue(LoaderStatus.failed)
        errorLiveData.postValue(exception.message)
        exception.printStackTrace()
    }

    init {
        errorMediatorLiveData.addSource(errorLiveData) { result: String? ->
            result?.let {
                errorMediatorLiveData.value = result
            }
        }
    }


    protected val exceptionHandler: CoroutineContext =
        CoroutineExceptionHandler { _, throwable ->
            isLoading.postValue(LoaderStatus.failed)
            errorLiveData.postValue(throwable.message)
            throwable.printStackTrace()
        }


    override fun onCleared() {
        super.onCleared()
        rootJob.cancel()
    }
}
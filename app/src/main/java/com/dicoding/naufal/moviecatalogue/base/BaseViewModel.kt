package com.dicoding.naufal.moviecatalogue.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.naufal.moviecatalogue.data.remote.network.MovieCatalogDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

open class BaseViewModel(application: Application, protected val dataSource: MovieCatalogDataSource) : AndroidViewModel(application) {
    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    protected val scope = CoroutineScope(coroutineContext)

    private val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun setLoading(state: Boolean){
        loadingLiveData.postValue(state)
    }

    fun isLoading(): LiveData<Boolean> = loadingLiveData

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
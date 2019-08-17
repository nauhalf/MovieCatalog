package com.dicoding.naufal.moviecatalogue.ui.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.moviecatalogue.base.BaseViewModel
import com.dicoding.naufal.moviecatalogue.data.remote.network.MovieCatalogDataSource
import com.dicoding.naufal.moviecatalogue.data.remote.network.Result
import com.dicoding.naufal.moviecatalogue.model.Film
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(application: Application, dataSource: MovieCatalogDataSource) :
    BaseViewModel(application, dataSource) {

    val listResultLiveData: MutableLiveData<List<Film>> = MutableLiveData()
    val searchTypeLiveData: MutableLiveData<Int> = MutableLiveData()
    val keywordLiveData: MutableLiveData<String> = MutableLiveData()
    val exceptionLiveData: MutableLiveData<Exception> = MutableLiveData()

    fun search(keyword: String) {
        if (searchTypeLiveData.value == 1) {
            searchMovie(keyword)
        } else {
            searchTv(keyword)
        }
    }

    fun searchMovie(keyword: String) {
        scope.launch {
            setLoading(true)
            keywordLiveData.value = keyword
            val searchResult = withContext(Dispatchers.IO) {
                dataSource.fetchSearchMovie(keywordLiveData.value!!)
            }

            when (searchResult) {
                is Result.Success -> {
                    listResultLiveData.value = searchResult.data
                }
                is Result.Error -> {
                    exceptionLiveData.value = searchResult.exception
                }
            }

            setLoading(false)
        }
    }

    fun searchTv(keyword: String) {
        scope.launch {
            keywordLiveData.value = keyword
            setLoading(true)
            val searchResult = withContext(Dispatchers.IO) {
                dataSource.fetchSearchTv(keywordLiveData.value!!)
            }
            when (searchResult) {
                is Result.Success -> {
                    listResultLiveData.value = searchResult.data
                }
                is Result.Error -> {
                    exceptionLiveData.value = searchResult.exception
                }
            }

            setLoading(false)
        }
    }
}
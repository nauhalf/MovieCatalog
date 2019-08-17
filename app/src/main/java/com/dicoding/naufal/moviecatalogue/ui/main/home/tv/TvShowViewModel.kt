package com.dicoding.naufal.moviecatalogue.ui.main.home.tv

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.moviecatalogue.R
import com.dicoding.naufal.moviecatalogue.base.BaseViewModel
import com.dicoding.naufal.moviecatalogue.data.local.favorite.FavoriteFilmDao
import com.dicoding.naufal.moviecatalogue.model.Film
import com.dicoding.naufal.moviecatalogue.data.remote.network.MovieCatalogDataSource
import com.dicoding.naufal.moviecatalogue.data.remote.network.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class TvShowViewModel(application: Application, dataSource: MovieCatalogDataSource) : BaseViewModel(application, dataSource){

    private val tvLiveData = MutableLiveData<List<Film>>()
    private val errorLiveData = MutableLiveData<Int>()

    fun getTvLiveData(): LiveData<List<Film>> = tvLiveData

    fun getErrorLiveData(): LiveData<Int> = errorLiveData

    init {
        fetchTvShow(1)
    }

    fun fetchTvShow(page: Int = 1) {
        scope.launch {
            setLoading(true)

            val tvResult = withContext(Dispatchers.IO) {
                dataSource.fetchDiscoveryTvShow(page)
            }

            when (tvResult) {
                is Result.Success -> {
                    tvLiveData.postValue(tvResult.data.film)
                }
                is Result.Error -> {
                    if(tvResult.exception is IOException){
                        errorLiveData.postValue(R.string.error_fetching_movies)
                    }
                }
            }
            setLoading(false)
        }
    }
}
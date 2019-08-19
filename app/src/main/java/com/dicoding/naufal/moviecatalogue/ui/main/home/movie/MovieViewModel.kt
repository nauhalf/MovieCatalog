package com.dicoding.naufal.moviecatalogue.ui.main.home.movie

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.moviecatalogue.R
import com.dicoding.naufal.moviecatalogue.base.BaseViewModel
import com.dicoding.naufal.moviecatalogue.model.Film
import com.dicoding.naufal.moviecatalogue.data.remote.network.MovieCatalogDataSource
import com.dicoding.naufal.moviecatalogue.data.remote.network.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MovieViewModel(application: Application, dataSource: MovieCatalogDataSource) : BaseViewModel(application, dataSource) {

     private val movieLiveData = MutableLiveData<List<Film>>()
    private val errorLiveData = MutableLiveData<Int>()

    fun getMovieLiveData(): LiveData<List<Film>> = movieLiveData

    fun getErrorLiveData(): LiveData<Int> = errorLiveData


    init {
        fetchMovie(1)
    }

    fun fetchMovie(page: Int = 1) {
        scope.launch {
            setLoading(true)

            val movieResult = withContext(Dispatchers.IO) {
                dataSource.fetchDiscoveryMovie(page)
            }

            when (movieResult) {
                is Result.Success -> {
                    movieLiveData.postValue(movieResult.data.film)
                }
                is Result.Error -> {
                    if(movieResult.exception is IOException){
                        errorLiveData.postValue(R.string.error_fetching_movies)
                    }
                }
            }
            setLoading(false)
        }
    }

}
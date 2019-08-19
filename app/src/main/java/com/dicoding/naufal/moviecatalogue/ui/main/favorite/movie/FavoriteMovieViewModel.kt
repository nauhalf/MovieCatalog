package com.dicoding.naufal.moviecatalogue.ui.main.favorite.movie

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.moviecatalogue.base.BaseViewModel
import com.dicoding.naufal.moviecatalogue.data.local.db.favorite.FavoriteFilm
import com.dicoding.naufal.moviecatalogue.data.remote.network.MovieCatalogDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteMovieViewModel(application: Application, dataSource: MovieCatalogDataSource) :
    BaseViewModel(application, dataSource) {

    private val movieLiveData = MutableLiveData<List<FavoriteFilm>>()
    private val errorLiveData = MutableLiveData<Int>()

    fun getMovieLiveData(): LiveData<List<FavoriteFilm>> = movieLiveData

    fun getErrorLiveData(): LiveData<Int> = errorLiveData

    init {
        fetchMovie()
    }

    private fun fetchMovie() {
        scope.launch {
            setLoading(true)

            val movieResult = withContext(Dispatchers.IO) {

                dataSource.database.favFilmDao().getFavMovie()


            }
            movieLiveData.postValue(movieResult)
            setLoading(false)
        }
    }

}
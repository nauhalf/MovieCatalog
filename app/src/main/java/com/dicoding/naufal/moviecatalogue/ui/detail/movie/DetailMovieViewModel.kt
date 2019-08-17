package com.dicoding.naufal.moviecatalogue.ui.detail.movie

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.moviecatalogue.R
import com.dicoding.naufal.moviecatalogue.base.BaseViewModel
import com.dicoding.naufal.moviecatalogue.data.local.favorite.FavoriteFilm
import com.dicoding.naufal.moviecatalogue.data.local.favorite.FavoriteFilmDao
import com.dicoding.naufal.moviecatalogue.model.Movie
import com.dicoding.naufal.moviecatalogue.data.remote.network.MovieCatalogDataSource
import com.dicoding.naufal.moviecatalogue.data.remote.network.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class DetailMovieViewModel(application: Application, dataSource: MovieCatalogDataSource) : BaseViewModel(application, dataSource) {

    private val isFavoriteLiveData = MutableLiveData<Boolean>()
    private val movieLiveData = MutableLiveData<Movie>()
    private val errorLiveData = MutableLiveData<Int>()
    private val movieIdLiveData = MutableLiveData<Int>()

    fun getMovieLiveData(): LiveData<Movie> = movieLiveData
    fun getErrorLiveData(): LiveData<Int> = errorLiveData

    fun setMovieId(movieId: Int){
        if(movieIdLiveData.value ==0 || movieIdLiveData.value == null) {
            movieIdLiveData.postValue(movieId)
            fetchMovie()
            fetchFav()
        }
    }

    private fun fetchFav(){
        scope.launch {
            val isFav: FavoriteFilm? = withContext(Dispatchers.IO){
                dataSource.database.favFilmDao().getFavMovie(movieIdLiveData.value)
            }

            isFavoriteLiveData.value = isFav != null
        }
    }


    private fun fetchMovie() {
        scope.launch {
            setLoading(true)

            val movieResult = withContext(Dispatchers.IO) {
                dataSource.fetchMovie(movieIdLiveData.value!!)
            }

            when (movieResult) {
                is Result.Success -> {
                    movieLiveData.postValue(movieResult.data)
                }
                is Result.Error -> {
                    if(movieResult.exception is IOException){
                        errorLiveData.postValue(R.string.error_fetching_movie)
                    }
                }
            }
            setLoading(false)
        }
    }

    fun addToFavorite() {
        scope.launch {
            withContext(Dispatchers.IO) {
                dataSource.database.favFilmDao().insertFavFilm(
                    FavoriteFilm(
                        filmId = movieIdLiveData.value!!,
                        filmType = 1
                    )
                )
            }

            isFavoriteLiveData.value = true
        }
    }

    fun deleteFromFavorite(){
        scope.launch {
            withContext(Dispatchers.IO) {
                dataSource.database.favFilmDao().deleteFavMovie(movieIdLiveData.value!!)
            }

            isFavoriteLiveData.value = false
        }
    }

    fun getIsFavoriteLiveData(): LiveData<Boolean> {
        return isFavoriteLiveData
    }
}
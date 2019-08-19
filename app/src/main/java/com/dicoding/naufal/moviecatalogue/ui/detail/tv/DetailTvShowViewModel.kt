package com.dicoding.naufal.moviecatalogue.ui.detail.tv

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.moviecatalogue.R
import com.dicoding.naufal.moviecatalogue.base.BaseViewModel
import com.dicoding.naufal.moviecatalogue.data.local.db.favorite.FavoriteFilm
import com.dicoding.naufal.moviecatalogue.data.remote.network.MovieCatalogDataSource
import com.dicoding.naufal.moviecatalogue.data.remote.network.Result
import com.dicoding.naufal.moviecatalogue.model.Tv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class DetailTvShowViewModel(application: Application, dataSource: MovieCatalogDataSource) :
    BaseViewModel(application, dataSource) {

    private val isFavoriteLiveData = MutableLiveData<Boolean>()
    private val tvLiveData = MutableLiveData<Tv>()
    private val errorLiveData = MutableLiveData<Int>()
    private val tvIdLiveData = MutableLiveData<Int>()

    fun getTvLiveData(): LiveData<Tv> = tvLiveData
    fun getErrorLiveData(): LiveData<Int> = errorLiveData
    fun getIsFavoriteLiveData(): LiveData<Boolean> = isFavoriteLiveData

    fun setTvId(tvId: Int) {
        if (tvIdLiveData.value == 0 || tvIdLiveData.value == null) {
            tvIdLiveData.postValue(tvId)
            fetchTv()
            fetchFav()
        }
    }

    private fun fetchFav() {
        scope.launch {
            val isFav: FavoriteFilm? = withContext(Dispatchers.IO) {
                dataSource.database.favFilmDao().getFavTv(tvIdLiveData.value)
            }

            isFavoriteLiveData.value = isFav != null
        }
    }

    private fun fetchTv() {
        scope.launch {
            setLoading(true)
            val tvResult = withContext(Dispatchers.IO) {
                dataSource.fetchTv(tvIdLiveData.value!!)
            }

            when (tvResult) {
                is Result.Success -> {
                    tvLiveData.postValue(tvResult.data)
                }
                is Result.Error -> {
                    if (tvResult.exception is IOException) {
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
                        filmId = tvIdLiveData.value!!,
                        filmType = 2,
                        filmPosterUrl = tvLiveData.value?.posterPath,
                        filmTitle = tvLiveData.value?.title
                    )
                )
            }



            isFavoriteLiveData.value = true
        }
    }

    fun deleteFromFavorite() {
        scope.launch {
            withContext(Dispatchers.IO) {
                dataSource.database.favFilmDao().deleteFavTv(tvIdLiveData.value!!)
            }

            isFavoriteLiveData.value = false
        }
    }
}
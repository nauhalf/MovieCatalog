package com.dicoding.naufal.moviecatalogue.ui.main.favorite.tv

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.moviecatalogue.base.BaseViewModel
import com.dicoding.naufal.moviecatalogue.data.local.db.favorite.FavoriteFilm
import com.dicoding.naufal.moviecatalogue.data.remote.network.MovieCatalogDataSource
import com.dicoding.naufal.moviecatalogue.model.Film
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteTvShowViewModel(application: Application, dataSource: MovieCatalogDataSource) :
    BaseViewModel(application, dataSource) {

    private val tvLiveData = MutableLiveData<List<FavoriteFilm>>()
    private val errorLiveData = MutableLiveData<Int>()

    fun getTvLiveData(): LiveData<List<FavoriteFilm>> = tvLiveData

    fun getErrorLiveData(): LiveData<Int> = errorLiveData

    init {
        fetchTvShow()
    }

    fun fetchTvShow() {
        scope.launch {
            setLoading(true)

            val tvResult = withContext(Dispatchers.IO) {

                dataSource.database.favFilmDao().getFavTv()
            }
            tvLiveData.postValue(tvResult)
            setLoading(false)
        }
    }
}
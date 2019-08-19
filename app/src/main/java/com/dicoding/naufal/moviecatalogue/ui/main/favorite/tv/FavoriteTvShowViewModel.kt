package com.dicoding.naufal.moviecatalogue.ui.main.favorite.tv

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.moviecatalogue.base.BaseViewModel
import com.dicoding.naufal.moviecatalogue.model.Film
import com.dicoding.naufal.moviecatalogue.data.remote.network.MovieCatalogDataSource
import com.dicoding.naufal.moviecatalogue.data.remote.network.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteTvShowViewModel(application: Application, dataSource: MovieCatalogDataSource) : BaseViewModel(application, dataSource){

    private val tvLiveData = MutableLiveData<List<Film>>()
    private val errorLiveData = MutableLiveData<Int>()

    fun getTvLiveData(): LiveData<List<Film>> = tvLiveData

    fun getErrorLiveData(): LiveData<Int> = errorLiveData

    init {
        fetchTvShow()
    }

    fun fetchTvShow() {
        scope.launch {
            setLoading(true)

            val tvResult = withContext(Dispatchers.IO) {

                val fav = dataSource.database.favFilmDao().getFavTv()
                val data = mutableListOf<Film>()

                for(i in fav){
                    when(val tv = dataSource.fetchTv(i.filmId)){
                        is Result.Success -> {
                            val film = Film(
                                id = tv.data.id,
                                title = tv.data.title,
                                releaseDate = tv.data.firstAirDate,
                                overview = tv.data.overview,
                                originalName = tv.data.originalName,
                                originalLanguage = tv.data.originalLanguage,
                                posterPath = tv.data.posterPath,
                                voteAverage = tv.data.voteAverage
                            )

                            data.add(film)
                        }
                    }
                }
                data
            }
            tvLiveData.postValue(tvResult)
            setLoading(false)
        }
    }
}
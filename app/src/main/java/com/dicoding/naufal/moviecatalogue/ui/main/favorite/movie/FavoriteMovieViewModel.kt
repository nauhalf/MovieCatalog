package com.dicoding.naufal.moviecatalogue.ui.main.favorite.movie

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.moviecatalogue.base.BaseViewModel
import com.dicoding.naufal.moviecatalogue.data.local.favorite.FavoriteFilmDao
import com.dicoding.naufal.moviecatalogue.model.Film
import com.dicoding.naufal.moviecatalogue.data.remote.network.MovieCatalogDataSource
import com.dicoding.naufal.moviecatalogue.data.remote.network.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteMovieViewModel(application: Application, dataSource: MovieCatalogDataSource) : BaseViewModel(application, dataSource) {

    private val movieLiveData = MutableLiveData<List<Film>>()
    private val errorLiveData = MutableLiveData<Int>()

    fun getMovieLiveData(): LiveData<List<Film>> = movieLiveData

    fun getErrorLiveData(): LiveData<Int> = errorLiveData

    init {
        fetchMovie()
    }

    private fun fetchMovie() {
        scope.launch {
            setLoading(true)

            val movieResult = withContext(Dispatchers.IO) {

                val fav = dataSource.database.favFilmDao().getFavMovie()
                val data = mutableListOf<Film>()

                for(i in fav){
                    when(val movie = dataSource.fetchMovie(i.filmId)){
                        is Result.Success -> {
                            val film = Film(
                                id = movie.data.id,
                                title = movie.data.title,
                                releaseDate = movie.data.releaseDate,
                                overview = movie.data.overview,
                                originalName = movie.data.originalTitle,
                                originalLanguage = movie.data.originalLanguage,
                                posterPath = movie.data.posterPath,
                                voteAverage = movie.data.voteAverage
                            )

                            data.add(film)
                        }
                    }
                }
                data
            }
            movieLiveData.postValue(movieResult)
            setLoading(false)
        }
    }

}
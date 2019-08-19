package com.dicoding.naufal.moviecatalogue.data.remote.network

import com.dicoding.naufal.moviecatalogue.data.local.db.MovieCatalogDatabase
import com.dicoding.naufal.moviecatalogue.model.Film
import com.dicoding.naufal.moviecatalogue.model.Films
import com.dicoding.naufal.moviecatalogue.model.Movie
import com.dicoding.naufal.moviecatalogue.model.Tv
import com.dicoding.naufal.moviecatalogue.utils.safeApiCall
import java.io.IOException

class MovieCatalogDataSource constructor(private val api: MovieCatalogApi, val database: MovieCatalogDatabase) {

    suspend fun fetchDiscoveryMovie(page: Int = 1) = safeApiCall {
        getDiscoveryMovie(page)
    }

    private suspend fun getDiscoveryMovie(page: Int = 1): Result<Films> {
        val response = api.getDiscoveryMovieAsync(page).await()
        return if (response.isSuccessful) {
            Result.Success(response.body()!!)
        } else {
            Result.Error(IOException())
        }

    }

    suspend fun fetchDiscoveryTvShow(page: Int = 1) = safeApiCall {
        getDiscoveryTv(page)
    }

    private suspend fun getDiscoveryTv(page: Int = 1): Result<Films> {
        val response = api.getDiscoveryTvAsync(page).await()
        return if (response.isSuccessful) {
            Result.Success(response.body()!!)
        } else {
            Result.Error(IOException())
        }
    }

    suspend fun fetchMovie(movieId: Int) = safeApiCall {
        getMovie(movieId)
    }

    private suspend fun getMovie(movieId: Int): Result<Movie> {
        val response = api.getMovieAsync(movieId).await()
        return if (response.isSuccessful) {
            Result.Success(response.body()!!)
        } else {
            Result.Error(IOException())
        }
    }

    suspend fun fetchTv(tvId: Int) = safeApiCall {
        getTv(tvId)
    }

    private suspend fun getTv(tvId: Int): Result<Tv> {
        val response = api.getTvAsync(tvId).await()
        return if (response.isSuccessful) {
            Result.Success(response.body()!!)
        } else {
            Result.Error(IOException())
        }
    }

    suspend fun fetchSearchMovie(query: String) = safeApiCall {
        searchMovie(query)
    }

    private suspend fun searchMovie(query: String): Result<List<Film>> {
        val response = api.searchMovieAsync(query).await()
        return if (response.isSuccessful) {
            val list = response.body()?.result?.filter { f ->
                f.originalLanguage == "ja"
            }
            Result.Success(list!!)
        } else {
            Result.Error(IOException())
        }
    }

    suspend fun fetchSearchTv(query: String) = safeApiCall {
        searchTv(query)
    }

    private suspend fun searchTv(query: String): Result<List<Film>> {
        val response = api.searchTvAsync(query).await()
        return if (response.isSuccessful) {
            val list = response.body()?.result?.filter { f ->
                f.originalLanguage == "ja"
            }
            Result.Success(list!!)
        } else {
            Result.Error(IOException())
        }
    }

    suspend fun fetchReleaseMovie(date: String) = safeApiCall {
        getReleaseMovie(date)
    }

    private suspend fun getReleaseMovie(date: String): Result<List<Film>> {
        val response = api.getReleaseMovieAsync(date, date).await()
        return if (response.isSuccessful) {
            val list = mutableListOf<Film>()

            response.body()?.film?.let {
                list.addAll(it)
            }

            Result.Success(list)
        } else {
            Result.Error(IOException())
        }
    }

    suspend fun fetchReleaseTv(date: String) = safeApiCall {
        getReleaseTv(date)
    }

    private suspend fun getReleaseTv(date: String): Result<List<Film>> {
        val response = api.getReleaseTvAsync(date, date).await()
        return if (response.isSuccessful) {
            val list = mutableListOf<Film>()

            response.body()?.film?.let {
                list.addAll(it)
            }

            Result.Success(list)
        } else {
            Result.Error(IOException())
        }
    }
}
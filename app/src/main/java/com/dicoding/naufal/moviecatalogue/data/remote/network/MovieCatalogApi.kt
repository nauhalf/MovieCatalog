package com.dicoding.naufal.moviecatalogue.data.remote.network

import com.dicoding.naufal.moviecatalogue.model.Films
import com.dicoding.naufal.moviecatalogue.model.Movie
import com.dicoding.naufal.moviecatalogue.model.SearchResult
import com.dicoding.naufal.moviecatalogue.model.Tv
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieCatalogApi {
    @GET("discover/movie?language=en-US&with_original_language=ja&with_genres=16")
    fun getDiscoveryMovieAsync(@Query("page") page: Int = 1): Deferred<Response<Films>>

    @GET("discover/tv?language=en-US&with_original_language=ja&with_genres=16")
    fun getDiscoveryTvAsync(@Query("page") page: Int = 1): Deferred<Response<Films>>

    @GET("movie/{movie_id}?language=en-US")
    fun getMovieAsync(@Path("movie_id") movieId: Int): Deferred<Response<Movie>>

    @GET("tv/{tv_id}?language=en-US")
    fun getTvAsync(@Path("tv_id") tvId: Int): Deferred<Response<Tv>>

    @GET("search/movie?language=en-US&region=JP")
    fun searchMovieAsync(@Query("query") query: String, @Query("Page") page: Int = 1): Deferred<Response<SearchResult>>

    @GET("search/tv?language=en-US")
    fun searchTvAsync(@Query("query") query: String, @Query("Page") page: Int = 1): Deferred<Response<SearchResult>>

    @GET("discover/movie?language=en-US&with_original_language=ja&with_genres=16")
    fun getReleaseMovieAsync(
        @Query("primary_release_date.gte") releaseDateG: String,
        @Query("primary_release_date.lte") releaseDateL: String
    ): Deferred<Response<Films>>

    @GET("discover/tv?language=en-US&with_original_language=ja&with_genres=16")
    fun getReleaseTvAsync(
        @Query("air_date.gte") airDateG: String,
        @Query("air_date.lte") airDateL: String
    ): Deferred<Response<Films>>
}
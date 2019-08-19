package com.dicoding.naufal.moviecatalogue.di

import com.dicoding.naufal.moviecatalogue.data.local.db.MovieCatalogDatabase
import com.dicoding.naufal.moviecatalogue.data.remote.network.MovieCatalogDataSource
import com.dicoding.naufal.moviecatalogue.data.remote.network.MovieCatalogRetrofitBuilder
import com.dicoding.naufal.moviecatalogue.ui.detail.movie.DetailMovieActivity
import com.dicoding.naufal.moviecatalogue.ui.detail.movie.DetailMovieViewModel
import com.dicoding.naufal.moviecatalogue.ui.detail.tv.DetailTvShowActivity
import com.dicoding.naufal.moviecatalogue.ui.detail.tv.DetailTvShowViewModel
import com.dicoding.naufal.moviecatalogue.ui.main.favorite.movie.FavoriteMovieFragment
import com.dicoding.naufal.moviecatalogue.ui.main.favorite.movie.FavoriteMovieViewModel
import com.dicoding.naufal.moviecatalogue.ui.main.favorite.tv.FavoriteTvShowFragment
import com.dicoding.naufal.moviecatalogue.ui.main.favorite.tv.FavoriteTvShowViewModel
import com.dicoding.naufal.moviecatalogue.ui.main.home.movie.MovieFragment
import com.dicoding.naufal.moviecatalogue.ui.main.home.movie.MovieViewModel
import com.dicoding.naufal.moviecatalogue.ui.main.home.tv.TvShowFragment
import com.dicoding.naufal.moviecatalogue.ui.main.home.tv.TvShowViewModel
import com.dicoding.naufal.moviecatalogue.ui.search.SearchActivity
import com.dicoding.naufal.moviecatalogue.ui.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single { MovieCatalogRetrofitBuilder.api() }
    single { MovieCatalogDatabase.getDatabase(get()) }
    single { MovieCatalogDataSource(get(), get()) }

    scope(named<TvShowFragment>()) {
        viewModel { TvShowViewModel(get(), get()) }
    }

    scope(named<MovieFragment>()) {
        viewModel { MovieViewModel(get(), get()) }
    }

    scope(named<FavoriteTvShowFragment>()) {
        viewModel { FavoriteTvShowViewModel(get(), get()) }
    }

    scope(named<FavoriteMovieFragment>()) {
        viewModel { FavoriteMovieViewModel(get(), get()) }
    }

    scope(named<DetailTvShowActivity>()) {
        viewModel { DetailTvShowViewModel(get(), get()) }
    }

    scope(named<DetailMovieActivity>()) {
        viewModel { DetailMovieViewModel(get(), get()) }
    }

    scope(named<SearchActivity>()) {
        viewModel { SearchViewModel(get(), get()) }
    }
}
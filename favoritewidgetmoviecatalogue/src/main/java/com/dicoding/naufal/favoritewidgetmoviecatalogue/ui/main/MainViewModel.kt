package com.dicoding.naufal.favoritewidgetmoviecatalogue.ui.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.favoritewidgetmoviecatalogue.base.BaseViewModel
import com.dicoding.naufal.favoritewidgetmoviecatalogue.data.model.FavoriteFilm
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : BaseViewModel(application) {

    val favFilmLiveData: MutableLiveData<List<FavoriteFilm>> = MutableLiveData()

    init {
        fetchData()
    }

    fun fetchData(){
        scope.launch {
            setLoading(true)
            val list = repository.fetchFavorite()
            favFilmLiveData.value = list
            setLoading(false)
        }
    }
}
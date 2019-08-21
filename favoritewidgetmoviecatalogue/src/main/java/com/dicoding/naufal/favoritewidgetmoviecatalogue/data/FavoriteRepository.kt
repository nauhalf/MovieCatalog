package com.dicoding.naufal.favoritewidgetmoviecatalogue.data

import android.content.Context
import com.dicoding.naufal.favoritewidgetmoviecatalogue.data.model.FavoriteFilm
import com.dicoding.naufal.favoritewidgetmoviecatalogue.data.provider.ContentProviderContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteRepository(val context: Context) {

    suspend fun fetchFavorite(): List<FavoriteFilm> {
        return withContext(Dispatchers.IO) {
            val list = mutableListOf<FavoriteFilm>()
            val cursorFav =
                context.contentResolver.query(ContentProviderContract.CONTENT_URI, null, null, null, null, null)

            if (cursorFav.moveToFirst() && cursorFav != null) {
                while (!cursorFav.isAfterLast) {
                    list.add(FavoriteFilm(cursorFav))
                    cursorFav.moveToNext()
                }
            }
            cursorFav.close()

            list
        }
    }
}
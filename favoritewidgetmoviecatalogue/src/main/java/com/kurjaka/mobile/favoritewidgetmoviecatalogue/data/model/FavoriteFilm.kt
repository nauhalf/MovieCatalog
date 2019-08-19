package com.kurjaka.mobile.favoritewidgetmoviecatalogue.data.model

import android.database.Cursor
import com.kurjaka.mobile.favoritewidgetmoviecatalogue.data.provider.ContentProviderContract.getColumnInt
import com.kurjaka.mobile.favoritewidgetmoviecatalogue.data.provider.ContentProviderContract.getColumnString

data class FavoriteFilm(
    var id: Int = 0,

    var filmId: Int = 0,

    var filmType: Int = 0,

    var filmPosterUrl: String? = null,

    var filmTitle: String? = null
) {
    companion object {
        const val TABLE_NAME = "fav_film"
        const val _ID = "_id"
        const val FILM_ID = "film_id"
        const val FILM_TYPE = "film_type"
        const val FILM_POSTER_URL = "film_poster_url"
        const val FILM_TITLE = "film_title"
    }

    constructor(cursor: Cursor) : this() {
        this.id = getColumnInt(cursor, _ID)
        this.filmId = getColumnInt(cursor, FILM_ID)
        this.filmTitle = getColumnString(cursor, FILM_TITLE)
        this.filmType = getColumnInt(cursor, FILM_TYPE)
        this.filmPosterUrl = getColumnString(cursor, FILM_POSTER_URL)
    }
}
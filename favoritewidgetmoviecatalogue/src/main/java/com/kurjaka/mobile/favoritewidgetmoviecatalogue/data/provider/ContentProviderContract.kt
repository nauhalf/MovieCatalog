package com.kurjaka.mobile.favoritewidgetmoviecatalogue.data.provider

import android.database.Cursor
import android.net.Uri
import com.kurjaka.mobile.favoritewidgetmoviecatalogue.data.model.FavoriteFilm.Companion.TABLE_NAME


object ContentProviderContract {

    const val AUTHORITY = "com.dicoding.naufal.moviecatalogue.data.local.provider"
    const val SCHEME = "content"

    val CONTENT_URI: Uri = Uri.Builder()
        .scheme(SCHEME)
        .authority(AUTHORITY)
        .appendPath(TABLE_NAME)
        .build()

    fun getColumnString(cursor: Cursor, columnName: String): String {
        return cursor.getString(cursor.getColumnIndex(columnName))
    }

    fun getColumnInt(cursor: Cursor, columnName: String): Int {
        return cursor.getInt(cursor.getColumnIndex(columnName))
    }

    fun getColumnLong(cursor: Cursor, columnName: String): Long {
        return cursor.getLong(cursor.getColumnIndex(columnName))
    }


}
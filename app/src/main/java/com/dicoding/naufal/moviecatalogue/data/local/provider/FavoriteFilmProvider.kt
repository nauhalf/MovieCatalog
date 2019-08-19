package com.dicoding.naufal.moviecatalogue.data.local.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.dicoding.naufal.moviecatalogue.data.local.db.MovieCatalogDatabase
import com.dicoding.naufal.moviecatalogue.data.local.db.favorite.FavoriteFilm
import org.koin.android.ext.android.inject

class FavoriteFilmProvider : ContentProvider(){
    val db: MovieCatalogDatabase by inject()

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        return when(mUriMatcher.match(uri)){
            FAVORITE_FILM -> {
                db.favFilmDao().providerGetFavFilm()
            }
            else -> null
        }
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    companion object{
        private const val FAVORITE_FILM = 1
        private val mUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        init{
            mUriMatcher.addURI(ContentProviderContract.AUTHORITY, FavoriteFilm.TABLE_NAME, FAVORITE_FILM)
        }
    }
}
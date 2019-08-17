package com.dicoding.naufal.moviecatalogue.data.local.favorite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = FavoriteFilm.TABLE_NAME)
data class FavoriteFilm(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = _ID)
    val id: Int = 0,

    @ColumnInfo(name = FILM_ID)
    val filmId: Int = 0,

    @ColumnInfo(name = FILM_TYPE)
    val filmType: Int = 0
) {
    companion object{
        const val TABLE_NAME = "fav_film"
        const val _ID = "_id"
        const val FILM_ID = "film_id"
        const val FILM_TYPE = "film_type"
    }
}
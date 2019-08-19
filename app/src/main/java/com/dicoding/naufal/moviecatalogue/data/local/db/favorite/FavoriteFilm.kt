package com.dicoding.naufal.moviecatalogue.data.local.db.favorite

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = FavoriteFilm.TABLE_NAME)
data class FavoriteFilm(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = _ID)
    var id: Int = 0,

    @ColumnInfo(name = FILM_ID)
    var filmId: Int = 0,

    @ColumnInfo(name = FILM_TYPE)
    var filmType: Int = 0
) : Parcelable {
    companion object {
        const val TABLE_NAME = "fav_film"
        const val _ID = "_id"
        const val FILM_ID = "film_id"
        const val FILM_TYPE = "film_type"
    }
}
package com.dicoding.naufal.moviecatalogue.data.local.favorite

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteFilmDao {
    @Query("SELECT * FROM ${FavoriteFilm.TABLE_NAME} WHERE ${FavoriteFilm.FILM_TYPE} = 1 ORDER BY ${FavoriteFilm._ID} ASC")
    suspend fun getFavMovie(): List<FavoriteFilm>

    @Query("SELECT * FROM ${FavoriteFilm.TABLE_NAME} WHERE ${FavoriteFilm.FILM_ID} = :filmId AND ${FavoriteFilm.FILM_TYPE} = 1 LIMIT 1")
    suspend fun getFavMovie(filmId: Int?): FavoriteFilm

    @Query("SELECT * FROM ${FavoriteFilm.TABLE_NAME} WHERE ${FavoriteFilm.FILM_TYPE} = 2 ORDER BY ${FavoriteFilm._ID} ASC")
    suspend fun getFavTv(): List<FavoriteFilm>

    @Query("SELECT * FROM ${FavoriteFilm.TABLE_NAME} WHERE ${FavoriteFilm.FILM_ID} = :filmId AND ${FavoriteFilm.FILM_TYPE} = 2 LIMIT 1")
    suspend fun getFavTv(filmId: Int?): FavoriteFilm

    @Insert
    suspend fun insertFavFilm(favMovie: FavoriteFilm) : Long

    @Query("DELETE FROM ${FavoriteFilm.TABLE_NAME} WHERE ${FavoriteFilm.FILM_ID} = :id AND ${FavoriteFilm.FILM_TYPE} = 2")
    suspend fun deleteFavTv(id: Int)

    @Query("DELETE FROM ${FavoriteFilm.TABLE_NAME} WHERE ${FavoriteFilm.FILM_ID} = :id AND ${FavoriteFilm.FILM_TYPE} = 1")
    suspend fun deleteFavMovie(id: Int)
}
package com.dicoding.naufal.moviecatalogue.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.naufal.moviecatalogue.data.local.db.favorite.FavoriteFilm
import com.dicoding.naufal.moviecatalogue.data.local.db.favorite.FavoriteFilmDao

@Database(entities = [FavoriteFilm::class], version = 1, exportSchema = true)
abstract class MovieCatalogDatabase() : RoomDatabase() {

    abstract fun favFilmDao(): FavoriteFilmDao

    companion object {
        @Volatile
        private var INSTANCE: MovieCatalogDatabase? = null

        fun getDatabase(context: Context): MovieCatalogDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieCatalogDatabase::class.java,
                    "movie_catalog_database"
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }

}
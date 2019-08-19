package com.kurjaka.mobile.favoritewidgetmoviecatalogue.ui.widget

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.kurjaka.mobile.favoritewidgetmoviecatalogue.R
import com.kurjaka.mobile.favoritewidgetmoviecatalogue.data.model.FavoriteFilm
import com.kurjaka.mobile.favoritewidgetmoviecatalogue.data.provider.ContentProviderContract.CONTENT_URI
import com.kurjaka.mobile.favoritewidgetmoviecatalogue.utils.MovieUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class FavoriteFilmRemoteViewsFactory(val context: Context) : RemoteViewsService.RemoteViewsFactory {

    private val list = mutableListOf<FavoriteFilm>()

    override fun onCreate() {

    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0L
    }

    override fun onDataSetChanged() {
        runBlocking(Dispatchers.Main) {
            val films = mutableListOf<FavoriteFilm>()
            val cursorFav = withContext(Dispatchers.IO) {
                context.contentResolver.query(CONTENT_URI, null, null, null, null, null)
            }
            if (cursorFav.moveToFirst() && cursorFav != null) {
                while (!cursorFav.isAfterLast) {
                    films.add(FavoriteFilm(cursorFav))
                    cursorFav.moveToNext()
                }
            }
            cursorFav.close()

            val id = Binder.clearCallingIdentity()
            list.clear()
            list.addAll(films)
            Binder.restoreCallingIdentity(id)
        }
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.favorite_film_item)
        val bitmap = Glide.with(context)
            .asBitmap()
            .load(MovieUtils.getImagePoster(list[position].filmPosterUrl))
            .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .get()

        rv.setImageViewBitmap(R.id.img_view, bitmap)
        val bundle = Bundle()
        bundle.putInt(FavoriteFilmWidget.EXTRA_FILM_ID, list[position].filmId)
        bundle.putInt(FavoriteFilmWidget.EXTRA_FILM_TYPE, list[position].filmType)
        val i = Intent()
        i.putExtras(bundle)
        rv.setOnClickFillInIntent(R.id.img_view, i)
        return rv
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun onDestroy() {

    }
}
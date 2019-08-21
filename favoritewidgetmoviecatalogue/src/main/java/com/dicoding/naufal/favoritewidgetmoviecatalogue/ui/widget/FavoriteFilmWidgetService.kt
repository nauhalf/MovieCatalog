package com.dicoding.naufal.favoritewidgetmoviecatalogue.ui.widget

import android.content.Intent
import android.widget.RemoteViewsService

class FavoriteFilmWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return FavoriteFilmRemoteViewsFactory(
            this.applicationContext
        )
    }
}
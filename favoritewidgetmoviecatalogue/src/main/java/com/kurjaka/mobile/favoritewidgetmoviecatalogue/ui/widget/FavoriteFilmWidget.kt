package com.kurjaka.mobile.favoritewidgetmoviecatalogue.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.widget.RemoteViews
import com.kurjaka.mobile.favoritewidgetmoviecatalogue.R


/**
 * Implementation of App Widget functionality.
 */
class FavoriteFilmWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId
            )

            updateWidget(context)
        }
    }

    private fun updateWidget(context: Context?) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisWidget = ComponentName(context, FavoriteFilmWidget::class.java)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view)
    }


    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        super.onReceive(context, intent)

        if (intent?.action != null) {
            if (intent.action == "UPDATE") {
                updateWidget(context)
            }

            if (intent.action == CLICK_ACTION) {
                val filmId = intent.getIntExtra(EXTRA_FILM_ID, 0)
                val filmType = intent.getIntExtra(EXTRA_FILM_TYPE, 0)
                if (filmType == 1) {
                    val i = Intent("com.dicoding.naufal.moviecatalogue.ui.detail.movie.DetailMovieActivity")
                    i.putExtra("MOVIE_ID", filmId)
                    i.addFlags(FLAG_ACTIVITY_NEW_TASK)
                    context?.startActivity(i)
                } else {
                    val i = Intent("com.dicoding.naufal.moviecatalogue.ui.detail.tv.DetailTvShowActivity")
                    i.addFlags(FLAG_ACTIVITY_NEW_TASK)
                    i.putExtra("TV_ID", filmId)
                    context?.startActivity(i)
                }
            }

        }

    }

    companion object {
        const val EXTRA_FILM_ID = "EXTRA_FILM_ID"
        const val EXTRA_FILM_TYPE = "EXTRA_FILM_TYPE"
        const val CLICK_ACTION = "CLICK_ACTION"

        internal fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val intent = Intent(context, FavoriteFilmWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

            val views = RemoteViews(context.packageName, R.layout.favorite_film_widget)
            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(R.id.stack_view, R.id.empty_view)

            val click = Intent(context, FavoriteFilmWidget::class.java)
            click.action =
                CLICK_ACTION
            click.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

            val clickPendingIntent =
                PendingIntent.getBroadcast(context, 0, click, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setPendingIntentTemplate(R.id.stack_view, clickPendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, views)

        }
    }


}


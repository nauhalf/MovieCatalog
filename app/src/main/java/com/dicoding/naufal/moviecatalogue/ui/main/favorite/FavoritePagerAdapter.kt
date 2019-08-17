package com.dicoding.naufal.moviecatalogue.ui.main.favorite

import android.content.Context
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.dicoding.naufal.moviecatalogue.R
import com.dicoding.naufal.moviecatalogue.ui.main.favorite.movie.FavoriteMovieFragment
import com.dicoding.naufal.moviecatalogue.ui.main.favorite.tv.FavoriteTvShowFragment
import com.dicoding.naufal.moviecatalogue.ui.main.home.movie.MovieFragment
import com.dicoding.naufal.moviecatalogue.ui.main.home.tv.TvShowFragment

class FavoritePagerAdapter(fragmentManager: FragmentManager, val context: Context) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteMovieFragment()
            else -> FavoriteTvShowFragment()
        }
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 ->context.getString(R.string.favorite_movie)
            else -> context.getString(R.string.favorite_tv_show)
        }
    }

    override fun saveState(): Parcelable? {
        return null
    }
}
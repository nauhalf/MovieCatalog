package com.dicoding.naufal.moviecatalogue.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dicoding.naufal.moviecatalogue.R
import com.dicoding.naufal.moviecatalogue.notification.daily.DailyReceiver
import com.dicoding.naufal.moviecatalogue.notification.released.ReleasedReceiver
import com.dicoding.naufal.moviecatalogue.ui.main.favorite.FavoriteFragment
import com.dicoding.naufal.moviecatalogue.ui.main.home.HomeFragment
import com.dicoding.naufal.moviecatalogue.ui.search.SearchActivity
import com.dicoding.naufal.moviecatalogue.ui.setting.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.template_toolbar.*


class MainActivity : AppCompatActivity() {

    private var mCurrentFragment: Fragment? = HomeFragment()
    private lateinit var mDailyReceiver: DailyReceiver
    private lateinit var mReleasedReceiver: ReleasedReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mDailyReceiver = DailyReceiver()
        mReleasedReceiver= ReleasedReceiver()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_main, mCurrentFragment!!)
                .commit()
        } else {
            mCurrentFragment = supportFragmentManager.getFragment(savedInstanceState, "FRAGMENT")
            supportFragmentManager.beginTransaction().replace(R.id.frame_main, mCurrentFragment!!).commit()

        }

        setUp()
    }

    private fun setUp() {
        setSupportActionBar(toolbar)
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        if(pref.getBoolean(getString(R.string.key_daily), true)){
            mDailyReceiver.setDailyReminder(this)
        } else {
            mDailyReceiver.cancelDailyReminder(this)
        }

        if(pref.getBoolean(getString(R.string.key_release), true)){
            mReleasedReceiver.setReleasedReminder(this)
        } else {
            mReleasedReceiver.cancelReleasedReminder(this)
        }
        bottom_nav_main.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    mCurrentFragment = HomeFragment()
                }

                R.id.menu_favorite -> {
                    mCurrentFragment = FavoriteFragment()

                }
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_main, mCurrentFragment!!)
                .commit()
            true

        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_search_film -> {
                AlertDialog.Builder(this)
                    .setItems(resources.getStringArray(R.array.search_type)) { _, position ->
                        when (position) {
                            0 -> {
                                startActivity(SearchActivity.newIntent(this, 1))
                            }
                            1 -> {
                                startActivity(SearchActivity.newIntent(this, 2))
                            }
                        }
                    }.setTitle(getString(R.string.choose_film_type))
                    .show()

                true
            }

            R.id.menu_setting -> {

                AlertDialog.Builder(this)
                    .setItems(resources.getStringArray(R.array.setting_type)) { _, position ->
                        when (position) {
                            0 -> {
                                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                                startActivity(intent)
                            }
                            1 -> {
                                startActivity(SettingsActivity.newIntent(this))
                            }
                        }
                    }.setTitle(getString(R.string.choose_setting_type))
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        supportFragmentManager.putFragment(outState, "FRAGMENT", mCurrentFragment!!);
    }
}
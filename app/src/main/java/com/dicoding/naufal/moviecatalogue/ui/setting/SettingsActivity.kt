package com.dicoding.naufal.moviecatalogue.ui.setting

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.naufal.moviecatalogue.R
import com.dicoding.naufal.moviecatalogue.notification.daily.DailyReceiver
import com.dicoding.naufal.moviecatalogue.notification.released.ReleasedReceiver
import kotlinx.android.synthetic.main.template_toolbar.*
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

        private lateinit var mDailyReminderPreference: SwitchPreference
        private lateinit var mReleaseReminderPreference: SwitchPreference

        private lateinit var daily: String
        private lateinit var release: String

        private lateinit var mDailyReceiver: DailyReceiver
        private lateinit var mReleasedReceiver: ReleasedReceiver

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)

            mDailyReceiver = DailyReceiver()
            mReleasedReceiver = ReleasedReceiver()

        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)


            setUp()
            setSummary()
        }

        fun setUp() {
            daily = getString(R.string.key_daily)
            release = getString(R.string.key_release)
            mDailyReminderPreference = findPreference(daily)!!
            mReleaseReminderPreference = findPreference(release)!!
        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
            if (key?.equals(daily) == true) {
                mDailyReminderPreference.isChecked = sharedPreferences?.getBoolean(daily, true)!!
                if (mDailyReminderPreference.isChecked) {
                    mDailyReceiver.setDailyReminder(requireContext())
                } else {
                    mDailyReceiver.cancelDailyReminder(requireContext())
                }
            }

            if (key?.equals(release) == true) {
                mReleaseReminderPreference.isChecked = sharedPreferences?.getBoolean(release, true)!!
                if (mReleaseReminderPreference.isChecked) {
                    mReleasedReceiver.setReleasedReminder(requireContext())
                } else {
                    mReleasedReceiver.cancelReleasedReminder(requireContext())
                }
            }
        }

        private fun setSummary() {
            val sh = preferenceManager.sharedPreferences
            mDailyReminderPreference.isChecked = sh.getBoolean(daily, true)
            mReleaseReminderPreference.isChecked = sh.getBoolean(release, true)
        }
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, SettingsActivity::class.java)
    }
}
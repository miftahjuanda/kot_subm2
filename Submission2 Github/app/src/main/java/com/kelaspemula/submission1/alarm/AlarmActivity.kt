package com.kelaspemula.submission1.alarm

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.kelaspemula.submission1.R
import kotlinx.android.synthetic.main.activity_alarm.*

class AlarmActivity : AppCompatActivity() {
    companion object {
        const val PREFERENCE = "preference"
        private const val REMINDER = "reminder"
    }

    private lateinit var alarmSetting : AlarmSetting
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.alarm)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        alarmSetting = AlarmSetting()
        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)

        setReminder()
        setAlarmCheck()
    }

    private fun setAlarmCheck() {
        reminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                alarmSetting.setNotification(this, AlarmSetting.TYPE, "09:00", "Lihat teman lainnya di Github")
            }else {
                alarmSetting.cancelReminder(this)
            }
            setChecked(isChecked)
        }
    }

    private fun setChecked(checked: Boolean) {
        val edit = sharedPreferences.edit()
        edit.putBoolean(REMINDER, checked)
        edit.apply()
    }

    private fun setReminder() {
        reminder.isChecked = sharedPreferences.getBoolean(REMINDER, false)
    }

    private fun setDaily(value: Boolean) {
        val edit = sharedPreferences.edit()
        edit.putBoolean(REMINDER, value)
        edit.apply()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
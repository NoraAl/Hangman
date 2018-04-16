package edu.umsl.hangman

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_title_view.*
import java.util.*

class MainView : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_view)

        playButton.setOnClickListener{
            val intent = Intent(this, GameView::class.java)
            this.startActivity(intent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        // notification will pop up after this amount of time

        val receiverIntent = Intent(this, NotificationReceiver::class.java)
        val broadcast = PendingIntent.getBroadcast(this, 0, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val calender = Calendar.getInstance()
        val timeInSecods = 8
        calender.add(Calendar.SECOND, timeInSecods)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calender.timeInMillis, broadcast)
    }
}



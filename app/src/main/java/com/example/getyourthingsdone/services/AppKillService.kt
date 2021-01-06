package com.example.getyourthingsdone.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.getyourthingsdone.R
import com.example.getyourthingsdone.models.SavePreferences

class AppKillService : Service() {


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val sharedPreferences = getSharedPreferences(resources.getString(R.string.shared_preferences_list), MODE_PRIVATE)
        val savePreferences = SavePreferences(sharedPreferences)
        savePreferences.saveNoteList()

    }
}
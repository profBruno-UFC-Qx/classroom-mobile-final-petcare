package com.example.petcare

import android.app.Application
import com.example.petcare.BuildConfig
import com.example.petcare.utils.NotificationUtils
import org.osmdroid.config.Configuration

class PetCareApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences("osmdroid", MODE_PRIVATE)
        )

        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

        NotificationUtils.createNotificationChannel(this)
    }
}

package com.example.skai

import android.app.Application
import com.example.skai.utils.NotificationService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SkaiApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Inicializar canal de notificaciones
        NotificationService.createNotificationChannel(this)
    }
}

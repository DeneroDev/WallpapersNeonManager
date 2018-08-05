package com.commonsneon.neonwallpapers.wallpapersneonmanager

import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.v4.content.FileProvider
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import java.io.File

/**
 * Created by Denero on 02.06.2018.
 */
class ImageAdapter(var data:Array<String>): RecyclerView.Adapter<ImageAdapter.ImViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImViewHolder = ImViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.fragment_image,parent,false))

    override fun onBindViewHolder(holder: ImViewHolder, position: Int) {
        Picasso.with(holder!!.v.context)
                .load(data[position])
                .fit()
                .into(holder.imgContent)

        holder.btnSet.setOnClickListener {
            var wallPaperManager = WallpaperManager.getInstance(holder.v.context)
            try {
                val bitmap = (holder.imgContent.getDrawable() as BitmapDrawable).bitmap
                wallPaperManager.setBitmap(bitmap)
                Toast.makeText(holder.v.context,"Completed!", Toast.LENGTH_SHORT)
                        .show()
            }catch(e:Exception){
                Toast.makeText(holder.v.context,"Wait loading", Toast.LENGTH_SHORT)
                        .show()
            }
        }

        holder.btnLivePhoto.setOnClickListener {
            instalLifeWallpapers(holder!!.v.context)
        }

    }


    override fun getItemCount(): Int = data.size



    class ImViewHolder(var v: View):RecyclerView.ViewHolder(v){
        var btnSet: Button = v.findViewById(R.id.btn_set)
        var imgContent: ImageView = v.findViewById(R.id.fragment_img)
        var btnLivePhoto:Button = v.findViewById(R.id.btn_livephoto)
    }

    fun instalLifeWallpapers(c: Context){
        try{
            var intentOpen = c.packageManager.getLaunchIntentForPackage(c.resources.getString(R.string.package_link))
            if(intentOpen == null){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    var intent = Intent(Intent.ACTION_VIEW)
                    var fileLifeWallpaper = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +"/"+ "LifeWallpapersNew"+".apk")
                    var uri = FileProvider.getUriForFile(c, c.getPackageName() + ".my.package.name.provider", fileLifeWallpaper)
                    intent.setDataAndType(uri, "application/vnd.android.package-archive")
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    c.startActivity(intent)
                }else{
                    var intent = Intent(Intent.ACTION_VIEW)
                    var fileLifeWallpaper = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +"/"+ "LifeWallpapersNew"+".apk")
                    var uri = Uri.fromFile(fileLifeWallpaper)
                    intent.setDataAndType(uri, "application/vnd.android.package-archive")
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    c.startActivity(intent)
                }
            }else{
                c.startActivity(intentOpen)
            }
        }catch(e:Exception){
            Toast.makeText(c,"WOU ERROR!",Toast.LENGTH_LONG).show()
        }
    }


}

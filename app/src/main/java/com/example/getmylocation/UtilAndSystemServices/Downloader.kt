package com.example.getmylocation.UtilAndSystemServices

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import java.net.URI

class Downloader() {

    fun downloadFromUri(context : Context ,uri : Uri, Title : String , Description : String){

        val downloader = DownloadManager.Request(uri)
            .setTitle(Title)
            .setDescription(Description)
            .setAllowedOverMetered(true)
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS , Title+".${uri.toString().substringAfterLast('.')}")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE or DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        downloadManager.enqueue(downloader)
        Toast.makeText(context , "Downloading .. ",Toast.LENGTH_SHORT).show()

    }
}
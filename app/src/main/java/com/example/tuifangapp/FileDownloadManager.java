package com.example.tuifangapp;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import com.example.Util.ToolsUtil;

public class FileDownloadManager {
	private DownloadManager downloadManager;
	private Context context;
    private static FileDownloadManager instance;
    private FileDownloadManager(Context context) {
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        this.context = context.getApplicationContext();
    }
    public static FileDownloadManager getInstance(Context context) {
        if (instance == null) {
            instance = new FileDownloadManager(context);
        }
        return instance;
    }
    
    public long startDownload(String uri, String title, String description,String appName) {
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(uri));
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //req.setAllowedOverRoaming(false);
 req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //设置文件的保存的位置[三种方式]
        //第一种
        //file:///storage/emulated/0/Android/data/your-package/files/Download/update.apk
        req.setDestinationInExternalFilesDir(context, ToolsUtil.getApkDir(), "Tuifang.apk");
        //第二种
        //file:///storage/emulated/0/Download/update.apk
//        req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Tuifang.apk");
        //第三种 自定义文件路径
        //req.setDestinationUri()


        // 设置一些基本显示信息
        req.setTitle(title);
        req.setDescription(description);
        req.setMimeType("application/vnd.android.package-archive");
        return downloadManager.enqueue(req);//异步
//        dm.openDownloadedFile()
    }
    
    public int getDownloadStatus(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                }
            } finally {
                c.close();
            }
        }
        return -1;
    }
    
    
    //获取文件保存的路径
    public String getDownloadPath(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getString(c.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));
                }
            } finally {
                c.close();
            }
        }
        return null;
    }
    
    //获取文件保存的地址
    public Uri getDownloadUri(long downloadId) {
        return downloadManager.getUriForDownloadedFile(downloadId);
    }

    public DownloadManager getDownloadManager() {
        return downloadManager;
    }

}

package com.example.Util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018\3\13 0013.
 */

public class PermissionUtils {
    private static final String TAG = PermissionUtils.class.getSimpleName();
    public static final int READ_WRITE = 1001;
    public static final int SMS_CONTACTS = 1002;
    public  static final int REQUEST1=1003;
    private static final String[] READ_AND_WRITE = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final String[] REQUEST = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final String[] READ_SMS_CONTACTS = {Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS};
    private static List<String> NOT_GET_PERMISSION = new ArrayList<>();

    /**
     * 6.0获取权限
     *
     * @return
     */
    private static boolean hasPermission(Context mContext, String[] permissions) {
        NOT_GET_PERMISSION.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                //判断用户是否对添加的权限授权
                if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "没有的权限>>>>>" + permission);
                    NOT_GET_PERMISSION.add(permission);
                }
            }
        }
        return NOT_GET_PERMISSION.size() == 0;
    }


    /**
     * 申请读写权限
     *
     * @param mContext 上下文
     * @return true 有权限 :  false 没有权限
     */
    public static boolean getReadWrite(Context mContext) {
        if (!hasPermission(mContext, READ_AND_WRITE)) {
            ActivityCompat.requestPermissions((Activity) mContext, NOT_GET_PERMISSION.toArray(new String[NOT_GET_PERMISSION.size()]), READ_WRITE);

                return false;

        }else {
            return true;
        }
    }



    /**
     * 申请读取短信和联系人
     *
     * @param mContext 上下文
     * @return true 有权限 :  false 没有权限
     */
    public static boolean readSmsContacts(Context mContext) {
        if (!hasPermission(mContext, READ_SMS_CONTACTS)) {
            ActivityCompat.requestPermissions((Activity) mContext, NOT_GET_PERMISSION.toArray(new String[NOT_GET_PERMISSION.size()]), SMS_CONTACTS);
            return false;
        } else {
            return true;
        }
    }
    public static boolean readrequest(Context mContext) {
        if (!hasPermission(mContext, REQUEST)) {
            ActivityCompat.requestPermissions((Activity) mContext, NOT_GET_PERMISSION.toArray(new String[NOT_GET_PERMISSION.size()]), REQUEST1);
            return false;
        } else {
            return true;
        }
    }
}

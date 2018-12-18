package com.example.Util;

import java.io.UnsupportedEncodingException;

import android.util.Base64;

public class codeutil {

	public static String getFromBase64(String str) {
        String result = "";  
        if (str != null) {  
            try {
                result = new String(Base64.decode(str, Base64.NO_WRAP), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }  
        }  
        return result;  
    } 

}

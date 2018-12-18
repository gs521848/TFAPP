package com.example.Util;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
* HTTP工具类
* 
* @ClassName HttpUtils
* @author Bob
* [url=home.php?mod=space&uid=354393]@DATE[/url] 2014-11-14 下午05:16:36
*/
public class HttpUtil {

//把Cookie定义为静态变量，第一次获取之后保存起来，以后每一次请求都设置给请求头即可
public static String COOKIE = "";

public static void doPost(String path, String param,HttpCallListener listener) {
try {
URL url = new URL(path);
HttpURLConnection connection = (HttpURLConnection) url.openConnection();

connection.setRequestMethod("POST");//设置为post请求
connection.setRequestProperty("Cookie", COOKIE);//将获取的Cookie存入请求头中发给服务器
connection.setDoInput(true);
connection.setDoOutput(true);

DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
outputStream.write(param.getBytes());
outputStream.flush();
outputStream.close();

//获取Cookie：从返回的消息头里的Set-Cookie的相应的值
COOKIE = connection.getHeaderField("Set-Cookie");


int code = connection.getResponseCode();
InputStream inputStream = connection.getInputStream();
String response = getString(inputStream);
if (code == HttpURLConnection.HTTP_OK) {
if (listener != null) {
listener.onSuccess(response);
}
} else {
if (listener != null) {
listener.onFailed("请求失败");
}
}
} catch (Exception e) {
// TODO: handle exception
e.printStackTrace();
if (listener != null) {
listener.onFailed("请求异常");
}
}
}

public static String getString(InputStream inputStream) throws Exception {
StringBuffer sb = new StringBuffer();
BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
String line = "";
while ((line = bufferedReader.readLine()) != null) {
sb.append(line);

}
bufferedReader.close();
return sb.toString();
}
}


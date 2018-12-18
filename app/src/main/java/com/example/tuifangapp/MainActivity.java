package com.example.tuifangapp;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.Util.Macutils;
import com.example.Util.PermissionUtils;
import com.example.Util.WebService;
import com.example.Util.codeutil;
import com.example.hookip.a;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.FormBody;
//import okhttp3.FormBody.Builder;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
/* compiled from: PhoneInfo */
public class MainActivity extends Activity {
	WebView webview;
	public static String hostmac;
	public static String encodehostweb = "aHR0cCUzQS8vYW5kcm9pZC5ydWFuc2t5LmNvbS9zZWFyY2glM0Z3b3JkJTNEJTI1RTklMjU4NyUyNTkxJTI1RTklMjU5MiUyNUIxJTI1RTYlMjVCOCUyNUI4JTI1RTYlMjU4OCUyNThG";
	public static String decodehostweb;
	int versioncode = 0;
	AlertDialog.Builder mdialog;
	public static String downUrl = "http://www.tuifang123.com/Images/Down/TuiFangAPP.apk";
	public static String updateinfo = "新版本更新，请下载后使用！";
	public static String updateurl="http://m.tuifang123.com/index.aspx";

	public static String Key = "eiz1GHxgxjGEtd644lo";
	public static String account = "17689949484";
	public String emptymac = "02:00:00:00:00:00";
	public ValueCallback<Uri[]> mUploadmsg;
	public ValueCallback<Uri> uploadMessage;
	private final static int FILE_CHOOSER_RESULT_CODE = 10000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//		forceUpdate(this, downUrl, updateinfo);
		StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.activity_main);
		if (!PermissionUtils.getReadWrite(this)) {

			Toast.makeText(this, "您尚未开启存储权限请自行开启,否则程序将出现异常", Toast.LENGTH_SHORT).show();
		}

		PermissionUtils.readrequest(this);
//		decode();
		gethostmac();
//		getcookie();


		webview = (WebView) findViewById(R.id.webView1);
		WebSettings settings = webview.getSettings();
		//设置javascript可用
		settings.setJavaScriptEnabled(true);
		settings.setAllowFileAccess(true);
		settings.setDomStorageEnabled(true);
		settings.setGeolocationEnabled(true);


		webview.loadUrl("http://m.tuifang123.com/index.aspx ");
		webview.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				ClipData mClipData = ClipData.newPlainText("Label",updateurl );
				cm.setPrimaryClip(mClipData);
				Toast.makeText(MainActivity.this,"链接已复制",Toast.LENGTH_SHORT).show();

				return false;
			}
		});
		webview.setWebViewClient(new WebViewClient() {
			@RequiresApi(api = Build.VERSION_CODES.M)
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {


				// TODO Auto-generated method stub
				if (url == null) return false;

				try {
					if (url.startsWith("weixin://") //微信
							|| url.startsWith("alipays://") //支付宝
							|| url.startsWith("mailto://") //邮件
							|| url.startsWith("tel://")//电话
							|| url.startsWith("dianping://")//大众点评
						//其他自定义的scheme
							) {
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
						startActivity(intent);
						return true;
					}
				} catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
					return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
				}

				//处理http和https开头的url
				view.loadUrl(url);
				updateurl=url;
				if (url.contains("userid")) {

					String accunt = url.substring(42, url.length());
					int newversion = WebService.postservice(MainActivity.this, accunt, hostmac, Key);
					int versioncode = getVersioncode();
					if (newversion > versioncode) {
						forceUpdate(MainActivity.this, downUrl, updateinfo);
					}

//		        	Toast.makeText(MainActivity.this, accunt, Toast.LENGTH_SHORT).show();
				}

				if (url.contains("tel:")) {
					String mobile = url.substring(url.lastIndexOf(":"), url.length());
					Toast.makeText(MainActivity.this, url + mobile, Toast.LENGTH_SHORT).show();
					Uri uri = Uri.parse("tel:" + mobile);
					Intent intent = new Intent(Intent.ACTION_CALL, uri);


					if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
						// TODO: Consider calling
						//    Activity#requestPermissions
						// here to request the missing permissions, and then overriding
						//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
						//                                          int[] grantResults)
						// to handle the case where the user grants the permission. See the documentation
						// for Activity#requestPermissions for more details.

					}
					startActivity(intent);

				}
		        return true;

			}


			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				
				
				CookieManager cookieManager = CookieManager.getInstance();
                String CookieStr = cookieManager.getCookie(url);
                if(CookieStr!=null)
                {
                    Log.i("cookie", CookieStr);
                }
                
				
                if (url.equals("http://m.tuifang123.com/index.aspx")) {
					
				}
                
                super.onPageFinished(view, url);
			}
		});


		
		webview.setWebChromeClient(new WebChromeClient(){

			//设置JsAlert框弹出内容
			@Override
			public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
				// TODO Auto-generated method stub
				if (!message.isEmpty()) {
					
					final AlertDialog ad= new AlertDialog.Builder(MainActivity.this).create();
					ad.show();
					Window window = ad.getWindow();
					window.setContentView(R.layout.activity_alert_demo);
					TextView tv_tit = (TextView) window.findViewById(R.id.alert_title);
					tv_tit.setText("提示");
					TextView tv_mess = (TextView) window.findViewById(R.id.alert_message);
					tv_mess.setText(message);
					Button bt_ok = (Button) window.findViewById(R.id.bt_ok);
					bt_ok.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							ad.dismiss();
							result.confirm();
						}
					});
					return true;
				}
//				ad.setTitle("提示")
//				  .setMessage(message)
//				  .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						result.confirm();
//					}
//				})
//				  .create().show();
				return false;
				
			}

			public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
				uploadMessage = valueCallback;
				openImageChooserActivity();
			}

			@Override
			public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
				mUploadmsg=filePathCallback;
				openImageChooserActivity();

				return true;
			}


		});


	}

public void openImageChooserActivity()
{
	Intent i = new Intent(Intent.ACTION_GET_CONTENT);
	i.addCategory(Intent.CATEGORY_OPENABLE);
	i.setType("image/*");
	startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
}




//强制更新
	private void forceUpdate(final Context context, final String downUrl, final String updateinfo) {
		mdialog = new AlertDialog.Builder(context);
		mdialog.setTitle("又更新咯！");
		mdialog.setMessage(updateinfo);
		mdialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

					DownLoadApk.download(context, downUrl, "版本更新啦", "Tuifang.apk");

            }
        }).setCancelable(false).create().show();
    }


//得到机子的mac地址
	private void gethostmac() {
		// TODO Auto-generated method stub
		
//		hostmac=getLocalMacAddress();
//		
//		if (hostmac.equals(emptymac)) {
			hostmac=a.bs(this);
//		}
		
		if (hostmac.equals("")) {
			hostmac=emptymac;
		}
		if (hostmac.equals(null))
		{
			hostmac=emptymac;
		}
//		if (hostmac.equals(" ")) {
//			hostmac=a.bs(this);
//		}
//		Toast.makeText(MainActivity.this, "本机mac:"+hostmac,Toast.LENGTH_LONG).show();
	}


	public String getLocalMacAddress()
	    {
			 WifiManager wifi = (WifiManager) getApplicationContext().getSystemService (Context.WIFI_SERVICE);
	        WifiInfo info = wifi.getConnectionInfo();
	  
	        return info.getMacAddress();
	    }
	public int getVersioncode()
	{
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(),0);

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        int versionCode = info.versionCode;
        return versionCode;


	}

	  
	  
//	Post账号 Mac 还有Key  
	  
//	 public void httppost()
//	 {
//		  OkHttpClient okHttpClient = new OkHttpClient();
//		  RequestBody body = new FormBody.Builder()
//				   .add("username", account)
//				   .add("mac", hostmac)
//				   .add("key", Key)
//				   .build();
//		  
//		  Request request=new Request.Builder()
//		  .url("http://117.25.149.230:9088/Service1.asmx?op=SetMac")
//		  .post(body)
//		  .build();
//		  
//		  Call newCall = okHttpClient.newCall(request);
//		  newCall.enqueue(new Callback() {
//			
//			@Override
//			public void onResponse(Call arg0, Response arg1) throws IOException {
//				// TODO Auto-generated method stub
//				Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
//				
//			}
//			
//			@Override
//			public void onFailure(Call arg0, IOException arg1) {
//				// TODO Auto-generated method stub
//				Toast.makeText(MainActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
//				
//			}
//		});
//		  
		  
		 
//	 }
	 public String getcookie()
	 {
		 URL url;
		try {
			url = new URL("tuifang123.com");
			HttpURLConnection conn =(HttpURLConnection) url.openConnection();
			cookiesss = conn.getHeaderField("set-cookie");
			
			return cookiesss;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cookiesss;
		 
		 
	 }


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}


	//base64解密
private void decode()
{
	decodehostweb=codeutil.getFromBase64(encodehostweb);
}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public boolean isFirst=true;
	private String cookiesss;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			if (webview.canGoBack()) {
				webview.goBack();
				return true;
			}else {
				Toast.makeText(this, "再按一次可退出", Toast.LENGTH_SHORT).show();
				if (isFirst) {
					isFirst=false;
					new Handler().postDelayed(new Runnable() {
						public void run() {
							isFirst=true;
						}
					}, 2000);
				}else {		
					webview.clearCache(true);
					
					finish();
					
				}
			}
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode!=FILE_CHOOSER_RESULT_CODE||mUploadmsg==null)
		{
			return;
		}
		if (null == uploadMessage && null == mUploadmsg) return;
		Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
		if (uploadMessage != null) {
			uploadMessage.onReceiveValue(result);
			uploadMessage = null;
		}
		Uri[] results = null;
		if (resultCode == Activity.RESULT_OK) {
			if (intent != null) {
				String dataString = intent.getDataString();
				ClipData clipData = intent.getClipData();
				if (clipData != null) {
					results = new Uri[clipData.getItemCount()];
					for (int i = 0; i < clipData.getItemCount(); i++) {
						ClipData.Item item = clipData.getItemAt(i);
						results[i] = item.getUri();
					}
				}
				if (dataString != null)
					results = new Uri[]{Uri.parse(dataString)};
			}
			mUploadmsg.onReceiveValue(results);
			mUploadmsg = null;
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

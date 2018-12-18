package com.example.Util;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class WebService {
	public static String namespace="http://tempuri.org/";
	public static String transUrl = "http://43.243.130.71:9088/Service1.asmx";  
	public static String method = "SetMac"; 
	public static int envolopeVersion = SoapEnvelope.VER12;
	public String SOAPAction= "http://tempuri.org/SetMac";
	public static int postservice(Context context,String username,String mac ,String key)
	{
		SoapObject request = new SoapObject(namespace, method);
		request.addProperty("username", username);
		request.addProperty("mac", mac);
		request.addProperty("key", key);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(envolopeVersion);
		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		HttpTransportSE se = new HttpTransportSE(transUrl);
		try {
			se.call(null, envelope);
			SoapObject response = (SoapObject) envelope.bodyIn;
			
			if (response!=null) {

				String reversion = response.toString();
				String strversion = reversion.substring(28, reversion.length()-3);
				int inversion=Integer.parseInt(strversion);
				Log.i("cookie", "上传成功");
				return inversion;

//				Toast.makeText(context, version, Toast.LENGTH_SHORT).show();

				
			}

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;

	}
	
		
	
	
	
	

}

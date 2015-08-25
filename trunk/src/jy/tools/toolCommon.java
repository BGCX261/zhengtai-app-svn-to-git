package jy.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import jy.TaoA.R;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

public class toolCommon {
	
	private static Context context;
	
	
	
	
	public static Context getContext(){
		return context;
	}
	
	
	
	public static void setContext(Context ctx){
		context = ctx;
	}
	
	
	
	public static void Msg(String txt){
		
		Toast.makeText(context, txt , Toast.LENGTH_SHORT).show();
		
	}
	
	
	public static void MsgWithBtn(String title,String info,String btn1,String btn2,DialogInterface.OnClickListener bt1,DialogInterface.OnClickListener bt2){
		
		new AlertDialog.Builder(context)
        .setIcon(R.drawable.icon)
        .setTitle(title)
        .setMessage(info)
        .setNegativeButton(btn1,bt1)
        .setPositiveButton(btn2,bt2)
        .show();
		
	}
	
	
	
	 public static String getMD5(String val) {  
	        MessageDigest md5 = null;
			try {
				md5 = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	        md5.update(val.getBytes());  
	        byte[] m = md5.digest();//º”√‹  
	        return getString(m);  
	}  
	 
	 
	 
	private static String getString(byte[] b){  
	        StringBuffer sb = new StringBuffer();  
	         for(int i = 0; i < b.length; i ++){  
	          sb.append(b[i]);  
	         }  
	         return sb.toString();  
	}  
	
	
	
	
	
	
	public static String getTime(){
		String re = "";
		
		re = getTime(0);
				
		return re;
	}
	
	
	
	public static String getTime(int addhours){
		String re = "";
		
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formater.setTimeZone(TimeZone.getTimeZone("PRC"));

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		cal.add(Calendar.HOUR_OF_DAY, addhours);
		re = formater.format(cal.getTime());

		return re;
	}


	
	public static Date getTime(String timestr, int addhours){
		
		Date re = new Date();
		
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formater.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		
		if(timestr != ""){
			try {
				re = formater.parse(timestr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(re);
		cal.setTimeZone(TimeZone.getTimeZone("PRC"));
		
		cal.add(Calendar.HOUR_OF_DAY, addhours);
		re = cal.getTime();

		return re;
	}

	
	public static String getTimeStr(Date itime){
		
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//formater.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		
		String re = formater.format(itime);
		
		return re;
	}
	
	
	
	
	
	
	
	public static String fileRead(File f){
		
		String re = "";
		
		FileReader fr;
		
		try {
			fr = new FileReader(f);
			
			char[] buf = new char[1024];   

            int len = fr.read(buf);
            
            re = new String(buf, 0, len);  
            
            fr.close();
            
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
			re = "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			re = "";
		}
		
		
		return re;
	}
	
	
	
	public static void fileWrite(File f,String txt){
		
		try {
			FileWriter fr = new FileWriter(f);

			fr.write(txt);
			
			fr.close();
            
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
}

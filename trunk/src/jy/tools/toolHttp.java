package jy.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class toolHttp {
	
	public static String Get(String url){
		
		String result = "";
	    HttpGet httpGet = new HttpGet(url); 
	    HttpClient httpClient = new DefaultHttpClient();  
	    HttpResponse httpResp;
	    
		try {
			httpResp = httpClient.execute(httpGet);
			if (httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
		        result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");  
		    } else {  
		        result = "";
		    }  
		} catch (ClientProtocolException e) {
			Log.v("httpget", "cpex");
			result="";
			e.printStackTrace();
		} catch (IOException e) {
			Log.v("httpget", "ioex"+e.getLocalizedMessage()+e.getMessage());
			result="";
			e.printStackTrace();
		}

		return result;
	}
	
	
	public static Bitmap GetImg(String url){
		
		Bitmap re = null ;
		
		//默认缓存2小时
		re = GetImg(url , 2);
		
		return re;
	}
	
	
	
	public static Bitmap GetImg(String url , int cacheHours){
		Bitmap re = null ;
		Log.v("filetest","jy.imageDown_获取图片开始，先尝试读soft缓存");
		
		
		
		//先尝试寻找内存中的对象
		re = toolImageCache.getPic(toolCommon.getMD5(url) + ".png");
		
		if(re != null){
			Log.v("filetest","jy.imageDown_从soft读缓存成功");
			return re;
		}
		Log.v("filetest","jy.imageDown_从soft读失败，尝试从本地图片缓存读取");
		
		
		
		//再尝试寻找本地图片缓存
		re = toolImageCache.getFile(toolCommon.getMD5(url) + ".png");
		
		if(re != null){
			Log.v("filetest","jy.imageDown_从本地图片缓存读取成功");
		}
		
		
		if(re == null){
			Log.v("filetest","jy.imageDown_从本地图片缓存读取失败，开始正经下载");
	
			//如果本地没有或已经过期，才去网上取
			InputStream restr = GetStream(url);
			
			if(restr != null){
				
				re = BitmapFactory.decodeStream(restr);
				
				if(re == null){
					Log.v("filetest","jy.imageDown_读图片流出错。。。");
				}else{
					Log.v("filetest","jy.imageDown_下载图片成功了。。。");
					
					//保存图片到本地缓存
					toolImageCache.addFile(toolCommon.getMD5(url) + ".png", re, cacheHours);
					
				}
			}else{
				Log.v("filetest","jy.imageDown_出错了，没有下载到图片。。。");
	
			}
		}
		
		
		if(re != null){
			
			//添加到软引用表
			SoftReference<Bitmap> bm = new SoftReference<Bitmap>(re);
			toolImageCache.addPic(toolCommon.getMD5(url) + ".png",bm);
			
		}
		
		Log.v("filetest","jy.imageDown_读取图片结束");
		
		return re;
		
	}
	

	public static InputStream GetStream(String url){
		InputStream re = null ;
		
		try{
	        URL feedUrl = new URL(url);
	        HttpURLConnection conn = (HttpURLConnection) feedUrl.openConnection();
	        conn.setConnectTimeout(10 * 1000);
	        //conn.addRequestProperty("If-Modified-Since", "Thu, 20 Jan 2011 07:15:35 GMT");
	        conn.connect();
	        Log.v("doInBack", "图片读取状态"+conn.getResponseCode());
	        re = conn.getInputStream();
	        
	    } catch (Exception e) {
	    	Log.v("doInBack", "图片读取获取数据流出错了！"+e.getMessage());
	        e.printStackTrace();
	        re = null;
	    }

	    
	    
		return re;
	}

}

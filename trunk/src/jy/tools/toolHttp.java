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
		
		//Ĭ�ϻ���2Сʱ
		re = GetImg(url , 2);
		
		return re;
	}
	
	
	
	public static Bitmap GetImg(String url , int cacheHours){
		Bitmap re = null ;
		Log.v("filetest","jy.imageDown_��ȡͼƬ��ʼ���ȳ��Զ�soft����");
		
		
		
		//�ȳ���Ѱ���ڴ��еĶ���
		re = toolImageCache.getPic(toolCommon.getMD5(url) + ".png");
		
		if(re != null){
			Log.v("filetest","jy.imageDown_��soft������ɹ�");
			return re;
		}
		Log.v("filetest","jy.imageDown_��soft��ʧ�ܣ����Դӱ���ͼƬ�����ȡ");
		
		
		
		//�ٳ���Ѱ�ұ���ͼƬ����
		re = toolImageCache.getFile(toolCommon.getMD5(url) + ".png");
		
		if(re != null){
			Log.v("filetest","jy.imageDown_�ӱ���ͼƬ�����ȡ�ɹ�");
		}
		
		
		if(re == null){
			Log.v("filetest","jy.imageDown_�ӱ���ͼƬ�����ȡʧ�ܣ���ʼ��������");
	
			//�������û�л��Ѿ����ڣ���ȥ����ȡ
			InputStream restr = GetStream(url);
			
			if(restr != null){
				
				re = BitmapFactory.decodeStream(restr);
				
				if(re == null){
					Log.v("filetest","jy.imageDown_��ͼƬ����������");
				}else{
					Log.v("filetest","jy.imageDown_����ͼƬ�ɹ��ˡ�����");
					
					//����ͼƬ�����ػ���
					toolImageCache.addFile(toolCommon.getMD5(url) + ".png", re, cacheHours);
					
				}
			}else{
				Log.v("filetest","jy.imageDown_�����ˣ�û�����ص�ͼƬ������");
	
			}
		}
		
		
		if(re != null){
			
			//��ӵ������ñ�
			SoftReference<Bitmap> bm = new SoftReference<Bitmap>(re);
			toolImageCache.addPic(toolCommon.getMD5(url) + ".png",bm);
			
		}
		
		Log.v("filetest","jy.imageDown_��ȡͼƬ����");
		
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
	        Log.v("doInBack", "ͼƬ��ȡ״̬"+conn.getResponseCode());
	        re = conn.getInputStream();
	        
	    } catch (Exception e) {
	    	Log.v("doInBack", "ͼƬ��ȡ��ȡ�����������ˣ�"+e.getMessage());
	        e.printStackTrace();
	        re = null;
	    }

	    
	    
		return re;
	}

}

package jy.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import jy.TaoA.MainActivity;
import jy.TaoA.R;
import jy.TaoA.models.modelImageLoader;
import jy.TaoA.models.modelItem;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

	//列表图片异步载入类
	public class toolImageLoaderLib{
		
		
		
		static BlockingQueue<Runnable> works = new ArrayBlockingQueue<Runnable>(10);
		
		
		
		private static ThreadPoolExecutor tp = new ThreadPoolExecutor(3, 5, 30, TimeUnit.SECONDS, works, new ThreadPoolExecutor.DiscardOldestPolicy());
		
		
		
		public static void getpic(modelImageLoader x){
			
			//new toolImageLoaderLib().new toolImageLoader().execute(x);
			
			tp.execute(new ThreadPoolTask(x));

			Log.v("xxx","xiancheng_count-"+tp.getPoolSize());
		}
		
		
		
		public static Handler imgLoaderHandler = new Handler() {  
	          public void handleMessage(Message msg) {   
	        	  
	               switch (msg.what) {
	                    case 1: //imgloader
	                        
	                    	Log.v("handler","handler收到通知-");
	                    	
	                    	modelImageLoader mi = (modelImageLoader) msg.obj;
	                    	
	                    	ImageView ff = mi.iv.get();
	                    	
	        		    	Log.v("haha", "back_"+(ff==null)+"-"+(mi==null)+"-"+(mi.bm==null));
	        		    	
	        		    	if(ff != null){
	        		    		ff.setImageBitmap(mi.bm);
	        		    	}
	        		    	
	        		    	
	        		    	//Log.v("imagedown", "jy.back_works xxx num " + works.size() + ", tp num" + tp.getTaskCount() + ",tp act num" + tp.getActiveCount() + "---" + mi.imgurl);
	        		    	
	                    	
	                         break;   
	               }
	               
	               super.handleMessage(msg);   
	          }   
	     };  


		
		//测试图片异步载入类，已废弃
		public class toolImageLoader_bak extends AsyncTask<modelImageLoader, modelImageLoader, Void> {
			
			//初始化
		    public toolImageLoader_bak() {
		    	
		    }
		    
		    
			@Override
			protected Void doInBackground(modelImageLoader... mi) {
				
				ImageView ff = mi[0].iv.get();
		    	
		    	if(ff != null){

		    		modelImageLoader thispic = mi[0];
				
					//读取图片
					Bitmap bitmap = toolHttp.GetImg(thispic.imgurl, 2);//缓存6小时

					Log.v("imgload", "imgload_去读取图片");
					
					//显示图片
		            if(bitmap != null){

			            bitmap = toolBitmap.GetThumb(bitmap, 225, 180);
			            
			            thispic.bm = bitmap;
			            
			            publishProgress(thispic);
		            }
		    	}

				return null;
			}

			
			protected void onProgressUpdate(modelImageLoader... mi){
				// TODO Auto-generated method stub
		    	if (isCancelled()){return;}
		    	
		    	
		    		
		    	ImageView ff = mi[0].iv.get();
		    	Log.v("haha", "back_"+(ff==null)+"-"+(mi[0]==null)+"-"+(mi[0].bm==null));
		    	
		    	if(ff != null){
		    		ff.setImageBitmap(mi[0].bm);
		    	}
		    	
		    	
		    	//Log.v("imagedown", "jy.back_works num " + works.size() + ", tp num" + tp.getTaskCount() + ",tp act num" + tp.getActiveCount());
		    	
			}

		    
		}

		
		
		public static class ThreadPoolTask implements Runnable,Serializable{
			
			private static final long serialVersionUID = 0;
			
			private modelImageLoader mi;
	
			ThreadPoolTask(modelImageLoader mii){
				this.mi = mii;
			}
			
			public void run(){
				
				if(mi.iv.get() != null){
					
					Log.v("imgload", "jy.imgload_ThreadPoolTask对象还在，去读取图片"+mi.imgurl);
					
					//读取图片
					Bitmap bitmap = toolHttp.GetImg(mi.imgurl, 6 );//缓存6小时
					
					Log.v("imgload", "jy.imgload_ThreadPoolTask对象还在，图片读取完成"+mi.imgurl);
					
					//显示图片
		            if(bitmap != null){
	
			            bitmap = toolBitmap.GetThumb(bitmap, 200, 200);
			            
			            mi.bm = bitmap;
			            
			            //Log.v("imgload", "jy.imgload_ThreadPoolTask读取图片完成，高度"+bitmap.getHeight());
			            
			            Message msg = new Message();
			            
			            msg.what = 1;
						
						msg.obj = mi;
	
						
						Log.v("imagedown", "jy.back_works num " + works.size() + ", tp num" + tp.getTaskCount() + ",tp act num" + tp.getActiveCount() + "---" + mi.imgurl);
						
						imgLoaderHandler.sendMessage(msg);
			            
						
						
		            }else{
		            	Log.v("imgload", "jy.imgload_ThreadPoolTask对象不在了，不读图片跳过"+mi.imgurl);
		            }
	            
				}
	            
				
			}
			
			public modelImageLoader getTask(){
				
				return this.mi;
				
			}

			
		}
	
	}
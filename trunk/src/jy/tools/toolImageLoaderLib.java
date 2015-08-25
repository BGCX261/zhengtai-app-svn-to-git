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

	//�б�ͼƬ�첽������
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
	                        
	                    	Log.v("handler","handler�յ�֪ͨ-");
	                    	
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


		
		//����ͼƬ�첽�����࣬�ѷ���
		public class toolImageLoader_bak extends AsyncTask<modelImageLoader, modelImageLoader, Void> {
			
			//��ʼ��
		    public toolImageLoader_bak() {
		    	
		    }
		    
		    
			@Override
			protected Void doInBackground(modelImageLoader... mi) {
				
				ImageView ff = mi[0].iv.get();
		    	
		    	if(ff != null){

		    		modelImageLoader thispic = mi[0];
				
					//��ȡͼƬ
					Bitmap bitmap = toolHttp.GetImg(thispic.imgurl, 2);//����6Сʱ

					Log.v("imgload", "imgload_ȥ��ȡͼƬ");
					
					//��ʾͼƬ
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
					
					Log.v("imgload", "jy.imgload_ThreadPoolTask�����ڣ�ȥ��ȡͼƬ"+mi.imgurl);
					
					//��ȡͼƬ
					Bitmap bitmap = toolHttp.GetImg(mi.imgurl, 6 );//����6Сʱ
					
					Log.v("imgload", "jy.imgload_ThreadPoolTask�����ڣ�ͼƬ��ȡ���"+mi.imgurl);
					
					//��ʾͼƬ
		            if(bitmap != null){
	
			            bitmap = toolBitmap.GetThumb(bitmap, 200, 200);
			            
			            mi.bm = bitmap;
			            
			            //Log.v("imgload", "jy.imgload_ThreadPoolTask��ȡͼƬ��ɣ��߶�"+bitmap.getHeight());
			            
			            Message msg = new Message();
			            
			            msg.what = 1;
						
						msg.obj = mi;
	
						
						Log.v("imagedown", "jy.back_works num " + works.size() + ", tp num" + tp.getTaskCount() + ",tp act num" + tp.getActiveCount() + "---" + mi.imgurl);
						
						imgLoaderHandler.sendMessage(msg);
			            
						
						
		            }else{
		            	Log.v("imgload", "jy.imgload_ThreadPoolTask�������ˣ�����ͼƬ����"+mi.imgurl);
		            }
	            
				}
	            
				
			}
			
			public modelImageLoader getTask(){
				
				return this.mi;
				
			}

			
		}
	
	}
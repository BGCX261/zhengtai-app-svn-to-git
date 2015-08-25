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

import jy.TaoA.models.modelImageLoader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;


	//检查更新
	public class toolUpdate extends AsyncTask<Void, String, Void> {
		
		//初始化
	    public toolUpdate() {
	    	
	    }

		@Override
		protected Void doInBackground(Void... xxx) {

			
			PackageManager pm = toolCommon.getContext().getPackageManager();
			PackageInfo pinfo = null;
			try {
				pinfo = pm.getPackageInfo(toolCommon.getContext().getPackageName(), PackageManager.GET_CONFIGURATIONS);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String versionName = pinfo.versionName;
			
			float ver = Float.parseFloat(versionName);
			
			
			String verUrl = "http://zhengtai.sinaapp.com/version.txt";
			String verre = toolHttp.Get(verUrl);
			
			float verOnline = Float.parseFloat(verre.split("\\|")[0]);
			
			if(verOnline > ver){
				publishProgress(verre);
			}
			

			return null;
		}

		
		protected void onProgressUpdate(final String... info){
	    	if (isCancelled()){return;}
	    	
	    	
	    	DialogInterface.OnClickListener bt1 = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	dialog.dismiss();
                }
			};
			
			
			DialogInterface.OnClickListener bt2 = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                	Intent intent= new Intent();        
                    intent.setAction("android.intent.action.VIEW");
                    intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                    Uri content_url = Uri.parse(info[0].split("\\|")[1]);   
                    //intent.setData(content_url); 
                    intent.setDataAndType(content_url,"application/vnd.android.package-archive");

                    toolCommon.getContext().startActivity(intent);
                    
                	dialog.dismiss();
                }
			};
			
			
			toolCommon.MsgWithBtn("应用更新", "正太君有新版本啦" + info[0].split("\\|")[1], "马上更新", "以后再说", bt2, bt1);
			
	    	

		}

	    
	}


package jy.TaoA;

import java.util.List;

import com.taobao.api.domain.TaobaokeItem;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import jy.TaoA.R;
import jy.TaoA.models.dataSource;
import jy.tools.toolCommon;
import jy.tools.toolHttp;
import jy.tools.toolImageCache;

public class Start extends Activity {
	
	
	public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        
        MobclickAgent.updateOnlineConfig(this);
        
        MobclickAgent.onError(this);
        
        MobclickAgent.setDebugMode(true);
        
        
        setContentView(R.layout.start);
        
        
        
		Thread thread = new Thread() {
			@Override
			public void run() {
				
		        String dataUrl = "http://zhengtai.sinaapp.com/out_index.php";
				
				String re = toolHttp.Get(dataUrl);
				
				Message message = handlerTest.obtainMessage();
				message.what = 1;
				message.obj = re;
				handlerTest.sendMessage(message);
				
			}
		};
		
		thread.start();
		thread = null;
		
        
        
    }
	
	
	
	//后续操作
	final Handler handlerTest = new Handler() {
		//@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			
			String re = "";
			
			switch(msg.what){  
			   
				case 1:
				   
					re = (String)msg.obj;
					
					if(re == null || re =="" ){
						toolCommon.setContext(getApplicationContext());
						toolCommon.Msg("网络不通哦亲,请稍后重试 :)");
						
						
						try {
							Thread.sleep(1500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						finish();
					}else{
						
						try {
							Thread.sleep(1500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						finish();
						Intent intent = new Intent();
				        intent.setClass(Start.this, MainActivity.class);
				        intent.putExtra("indexdata",re);
				        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				        startActivity(intent);
					}
			
					break;
				
			}
			
		}
	};
	
	
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onResume(this);
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}
	
	
}
	

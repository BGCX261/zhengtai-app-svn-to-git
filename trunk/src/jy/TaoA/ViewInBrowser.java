package jy.TaoA;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import jy.TaoA.R;
import jy.tools.toolCommon;

public class ViewInBrowser extends Activity {
	
	private WebView webView1;
	
	private String url;
	
	public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
 
        setContentView(R.layout.viewinbrowser);
        
        webView1=(WebView)findViewById(R.id.webview1);  
        
        
        webView1.getSettings().setJavaScriptEnabled(true);
        //webView1.clearCache(true);
        //webView1.setScrollBarStyle(RESULT_CANCELED);
        webView1.setWebViewClient(new MyWebViewClient()); 
        webView1.setLongClickable(false);
        webView1.getSettings().setDomStorageEnabled(true);
        
        
        url = this.getIntent().getExtras().getString("tourl");
        
        //url = "http://www.xfocus.net/";
        
        //toolCommon.Msg(url);
        
        webView1.loadUrl(url);

    }
	
	
	
	//web视图客户端   
    public class MyWebViewClient extends WebViewClient   
    {   
        public boolean shouldOverviewUrlLoading(WebView view,String url)   
        {   
            view.loadUrl(url);   
            return true;   
        }   
    }
    
    
    
    
    //设置回退   
    public boolean onKeyDown(int keyCode,KeyEvent event)   
    {   
    	
        if((keyCode==KeyEvent.KEYCODE_BACK))   
        {   
        	if(webView1.canGoBack()){
	        	webView1.goBack();   
		        return true;
        	}
        }
        
        return super.onKeyDown(keyCode,event);   
    }   
    
    
    
    
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onResume(this);
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}
	
	
    
}

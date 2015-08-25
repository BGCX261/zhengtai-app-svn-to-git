package jy.TaoA;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.taobao.api.domain.TaobaokeItem;
import com.umeng.analytics.MobclickAgent;

import jy.TaoA.R;
import jy.TaoA.ViewInBrowser.MyWebViewClient;
import jy.TaoA.models.dataSource;
import jy.TaoA.models.modelImageLoader;
import jy.TaoA.models.modelIndexData;
import jy.TaoA.models.modelItem;
import jy.tools.toolBitmap;
import jy.tools.toolHttp;
import jy.tools.toolCommon;
import jy.tools.toolImageCache;
import jy.tools.toolImageLoaderLib;
import jy.tools.toolUpdate;
import android.R.id;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;


public class MainActivity extends Activity {

	private TabHost tabHost;
	private TabWidget tabWidget;

	//滚动条用
	private TextView ib5_ct ;
	float density= 0.0f;
	
	
	public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
 
        toolCommon.setContext(this);
        
        setContentView(R.layout.main);
        
        
        
        //初始化主界面
        initViews();
        
        
        //检查更新
        new toolUpdate().execute();
        
        
        String re = this.getIntent().getExtras().getString("indexdata");
        
		
		final modelIndexData[] indexdata = new modelIndexData[6];
		
		JSONObject js;
        
		try {
			js = new JSONObject(re);
			JSONArray jsa = js.getJSONArray("datas");
			
	        for (int i = 0; i < jsa.length(); i++) {
	        	
				JSONObject jo = (JSONObject) jsa.opt(i);
				
				String name = jo.getString("name");
				String classid = jo.getString("classid");
				String imgurl = jo.getString("picurl");
				
				indexdata[i] = new modelIndexData(name,classid,imgurl);
				
	        }
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("hahaha", "hahaha"+e.getMessage());
		}
		
		
		
        
        ImageView iv1 = (ImageView)findViewById(R.id.zhengt_1);
        ImageView iv2 = (ImageView)findViewById(R.id.zhengt_2);
        ImageView iv3 = (ImageView)findViewById(R.id.zhengt_3);
		ImageView iv4 = (ImageView)findViewById(R.id.zhengt_4);
		ImageView iv5 = (ImageView)findViewById(R.id.zhengt_5);
		ImageView iv6 = (ImageView)findViewById(R.id.zhengt_6);
		
		TextView tv1 = (TextView)findViewById(R.id.zhengt_title_1);
		TextView tv2 = (TextView)findViewById(R.id.zhengt_title_2);
		TextView tv3 = (TextView)findViewById(R.id.zhengt_title_3);
		TextView tv4 = (TextView)findViewById(R.id.zhengt_title_4);
		TextView tv5 = (TextView)findViewById(R.id.zhengt_title_5);
		TextView tv6 = (TextView)findViewById(R.id.zhengt_title_6);
		
		
		
		tv1.setText(indexdata[0].title);
		tv2.setText(indexdata[1].title);
		tv3.setText(indexdata[2].title);
		tv4.setText(indexdata[3].title);
		tv5.setText(indexdata[4].title);
		tv6.setText(indexdata[5].title);

        WeakReference img1 = new WeakReference(iv1);
		toolImageLoaderLib.getpic(new modelImageLoader(img1, indexdata[0].picurl));

        WeakReference img2 = new WeakReference(iv2);
		toolImageLoaderLib.getpic(new modelImageLoader(img2,indexdata[1].picurl));

        WeakReference img3 = new WeakReference(iv3);
		toolImageLoaderLib.getpic(new modelImageLoader(img3,indexdata[2].picurl));

        WeakReference img4 = new WeakReference(iv4);
		toolImageLoaderLib.getpic(new modelImageLoader(img4,indexdata[3].picurl));

        WeakReference img5 = new WeakReference(iv5);
		toolImageLoaderLib.getpic(new modelImageLoader(img5,indexdata[4].picurl));

        WeakReference img6 = new WeakReference(iv6);
		toolImageLoaderLib.getpic(new modelImageLoader(img6,indexdata[5].picurl));

		
		 iv1.setOnClickListener( new ImageView.OnClickListener()
	        {
	            public void onClick( View v )
	            {
	            	Intent intent = new Intent();
	                intent.setClass(MainActivity.this, ItemListTop.class);
	                intent.putExtra("classid", indexdata[0].title + "-" + indexdata[0].classid);
	                startActivity(intent);
	            }
	            
	        } );

		 iv2.setOnClickListener( new ImageView.OnClickListener()
	        {
	            public void onClick( View v )
	            {
	            	Intent intent = new Intent();
	                intent.setClass(MainActivity.this, ItemListTop.class);
	                intent.putExtra("classid", indexdata[1].title + "-" + indexdata[1].classid);
	                startActivity(intent);
	            }
	            
	        } );

		 iv3.setOnClickListener( new ImageView.OnClickListener()
	        {
	            public void onClick( View v )
	            {
	            	Intent intent = new Intent();
	                intent.setClass(MainActivity.this, ItemListTop.class);
	                intent.putExtra("classid", indexdata[2].title + "-" + indexdata[2].classid);
	                startActivity(intent);
	            }
	            
	        } );

		 iv4.setOnClickListener( new ImageView.OnClickListener()
	        {
	            public void onClick( View v )
	            {
	            	Intent intent = new Intent();
	                intent.setClass(MainActivity.this, ItemListTop.class);
	                intent.putExtra("classid", indexdata[3].title + "-" + indexdata[3].classid);
	                startActivity(intent);
	            }
	            
	        } );

		 iv5.setOnClickListener( new ImageView.OnClickListener()
	        {
	            public void onClick( View v )
	            {
	            	Intent intent = new Intent();
	                intent.setClass(MainActivity.this, ItemListTop.class);
	                intent.putExtra("classid", indexdata[4].title + "-" + indexdata[4].classid);
	                startActivity(intent);
	            }
	            
	        } );

		 iv6.setOnClickListener( new ImageView.OnClickListener()
	        {
	            public void onClick( View v )
	            {
	            	Intent intent = new Intent();
	                intent.setClass(MainActivity.this, ItemListTop.class);
	                intent.putExtra("classid", indexdata[5].title + "-" + indexdata[5].classid);
	                startActivity(intent);
	            }
	            
	        } );
        
		 
		 
		 
		 
		 
		 
		 
		 //震动体验tab
		 Button bt1= (Button)findViewById(R.id.zdbtn);
		 Button bt2= (Button)findViewById(R.id.zdbtn2);
		 Button bt3= (Button)findViewById(R.id.zdbtn3);
		 Button bt4= (Button)findViewById(R.id.zdbtn4);
		 Button bt5= (Button)findViewById(R.id.zdbtn5);
		 
		 final Vibrator mVibrator01 = ( Vibrator ) getApplication().getSystemService(Service.VIBRATOR_SERVICE);  
		 
		 
		 bt1.setOnClickListener( new Button.OnClickListener()
	    {
	            public void onClick( View v )
	            {
	            	mVibrator01.cancel();
	            	
	            	mVibrator01.vibrate( new long[]{400,200,200,200},0);    

	            }
	            
	    } );
		 
		 bt2.setOnClickListener( new Button.OnClickListener()
		    {
		            public void onClick( View v )
		            {
		            	mVibrator01.cancel();
		            	
		            	mVibrator01.vibrate( new long[]{100,200,100,200},0);   

		            }
		            
		    } );
		 	 
		 bt3.setOnClickListener( new Button.OnClickListener()
		    {
		            public void onClick( View v )
		            {
		            	mVibrator01.cancel();
		            	
		            	mVibrator01.vibrate( new long[]{100,1000,100,1000},0);    

		            }
		            
		    } );
		 	 
		 bt4.setOnClickListener( new Button.OnClickListener()
		    {
		            public void onClick( View v )
		            {
		            	mVibrator01.cancel();
		            	
		            	toolCommon.Msg("我只是个手机啊亲......");

		            }
		            
		    } );
		 	 
		 bt5.setOnClickListener( new Button.OnClickListener()
		    {
		            public void onClick( View v )
		            {
		            	mVibrator01.cancel();
		            }
		            
		    } );
		 
		 
		 
		 
		 
		 //个人中心tab http://my.m.taobao.com/myTaobao.htm
		 
		 TextView tab3title=(TextView)findViewById(R.id.top_title3);
		 tab3title.setText("请用淘宝帐号登录");
		  
		 WebView webView1=(WebView)findViewById(R.id.webview1);

		 webView1.getSettings().setJavaScriptEnabled(true);
	     webView1.setWebViewClient(new ViewInBrowser().new MyWebViewClient()); 
	     webView1.setLongClickable(false);
	     webView1.loadUrl("http://my.m.taobao.com/myTaobao.htm");
        
    }
	
	

	
	//初始化页面控件
	public void initViews(){
		
        
		toolImageCache.loadFileCache();
		
        
        //初始化tabHost
        this.tabHost = (TabHost) findViewById(R.id.tabhost); 
        
        //初始化tabHost内布局
        this.tabHost.setup();
        LayoutInflater inflater_tab1 = LayoutInflater.from(this);   
        inflater_tab1.inflate(R.layout.tab_main, tabHost.getTabContentView());  
        inflater_tab1.inflate(R.layout.tab_shake, tabHost.getTabContentView());  
        inflater_tab1.inflate(R.layout.viewinbrowser, tabHost.getTabContentView()); 
        inflater_tab1.inflate(R.layout.tab_more, tabHost.getTabContentView()); 
        
        //设置tabHost
        tabWidget = (TabWidget) findViewById(android.R.id.tabs);  
        tabWidget.setBackgroundColor(Color.BLUE);
        tabHost.addTab(tabHost.newTabSpec("1").setIndicator("tab1").setContent(R.id.LinearLayout01)); 
        tabHost.addTab(tabHost.newTabSpec("2").setIndicator("tab2").setContent(R.id.LinearLayout02)); 
        tabHost.addTab(tabHost.newTabSpec("3").setIndicator("tab3").setContent(R.id.LinearLayoutWebView)); 
        tabHost.addTab(tabHost.newTabSpec("4").setIndicator("tab4").setContent(R.id.LinearLayout03)); 
        
        //初始化tab按钮
        final TextView ib3=(TextView)findViewById(R.id.b3);
        final TextView ib2=(TextView)findViewById(R.id.b2);  
        final TextView ib1=(TextView)findViewById(R.id.b1);
        final TextView ib4=(TextView)findViewById(R.id.b4);
        

        //定义tab按钮动作
        ib1.setOnClickListener( new TextView.OnClickListener()
        {
            public void onClick( View v )
            {
            	tabHost.setCurrentTabByTag("1");
            	
            	//moveIb5(0);
            	
            	ib1.setTextColor(getApplicationContext().getResources().getColor(R.color.Bbai));
            	ib2.setTextColor(getApplicationContext().getResources().getColor(R.color.huizi));
            	ib3.setTextColor(getApplicationContext().getResources().getColor(R.color.huizi));
            	ib4.setTextColor(getApplicationContext().getResources().getColor(R.color.huizi));
            	
            	ib1.setBackgroundResource(R.drawable.icon_1_on);
            	ib2.setBackgroundResource(R.drawable.icon_2_off);
            	ib3.setBackgroundResource(R.drawable.icon_3_off);
            	ib4.setBackgroundResource(R.drawable.icon_4_off);
            }
        } );
        
        ib2.setOnClickListener( new TextView.OnClickListener()
        {
            public void onClick( View v )
            {
            	tabHost.setCurrentTabByTag("2");

            	
            	//moveIb5(1);
            	
            	ib2.setTextColor(getApplicationContext().getResources().getColor(R.color.Bbai));
            	ib1.setTextColor(getApplicationContext().getResources().getColor(R.color.huizi));
            	ib3.setTextColor(getApplicationContext().getResources().getColor(R.color.huizi));
            	ib4.setTextColor(getApplicationContext().getResources().getColor(R.color.huizi));
            	

            	ib1.setBackgroundResource(R.drawable.icon_1_off);
            	ib2.setBackgroundResource(R.drawable.icon_2_on);
            	ib3.setBackgroundResource(R.drawable.icon_3_off);
            	ib4.setBackgroundResource(R.drawable.icon_4_off);
            }
            
        } );
        
        ib3.setOnClickListener( new TextView.OnClickListener()
        {
            public void onClick( View v )
            {
            	tabHost.setCurrentTabByTag("3");

            	//moveIb5(2);
            	
            	ib3.setTextColor(getApplicationContext().getResources().getColor(R.color.Bbai));
            	ib1.setTextColor(getApplicationContext().getResources().getColor(R.color.huizi));
            	ib2.setTextColor(getApplicationContext().getResources().getColor(R.color.huizi));
            	ib4.setTextColor(getApplicationContext().getResources().getColor(R.color.huizi));
            	
            	ib1.setBackgroundResource(R.drawable.icon_1_off);
            	ib2.setBackgroundResource(R.drawable.icon_2_off);
            	ib3.setBackgroundResource(R.drawable.icon_3_on);
            	ib4.setBackgroundResource(R.drawable.icon_4_off);
            	
            }
        } );
        
        
        ib4.setOnClickListener( new TextView.OnClickListener()
        {
            public void onClick( View v )
            {
            	tabHost.setCurrentTabByTag("4");

            	//moveIb5(2);
            	
            	ib4.setTextColor(getApplicationContext().getResources().getColor(R.color.Bbai));
            	ib1.setTextColor(getApplicationContext().getResources().getColor(R.color.huizi));
            	ib2.setTextColor(getApplicationContext().getResources().getColor(R.color.huizi));
            	ib3.setTextColor(getApplicationContext().getResources().getColor(R.color.huizi));
            	
            	ib4.setBackgroundResource(R.drawable.icon_4_on);
            	ib1.setBackgroundResource(R.drawable.icon_1_off);
            	ib2.setBackgroundResource(R.drawable.icon_2_off);
            	ib3.setBackgroundResource(R.drawable.icon_3_off);
            	
            }
        } );
        
        
        
        
        
        
        
	}
	
	

	//tab切换特效
	public void moveIb5(final int tabindex){

		final TextView ib5=(TextView)findViewById(R.id.b5);
        ib5_ct = (TextView)findViewById(R.id.b5);
        Log.v("tabmove", "开始前，现在在" + ib5_ct.getLeft() + "," + ib5_ct.getTop() + "," + ib5_ct.getRight() + "," + ib5_ct.getBottom());
		
        DisplayMetrics dm = new DisplayMetrics();  
    	dm = getResources().getDisplayMetrics();  
    	density= dm.density;
    	
    	Log.v("tabmove", "density = " + density);
    	
		int gox = ((tabindex*106) + 28);
		Log.v("tabmove", "准备去" + gox + "dp处");
    	int ib5move = (int)(gox*density) - ib5_ct.getLeft();
    	
    	Log.v("tabmove", "移动" + ib5move + "px,从" + ib5_ct.getLeft() + "到" + (int)(gox*density));
    	
    	
    	//int top = ib5_ct.getTop();
    	//int right = (int)ib5_ct.getWidth() + (int)gox;
    	//int buttom = ib5_ct.getHeight()+ib5_ct.getTop();
    	
    	//ib5_ct.layout((int)(gox * density), top , right , buttom);
    	//Log.v("tabmove", "已完成，现在在" + (int)(gox * density) + "," + top + "," + right + "," + buttom);
    	//Log.v("tabmove", "已完成，现在在" + ib5_ct.getLeft() + "," + ib5_ct.getTop() + "," + ib5_ct.getRight() + "," + ib5_ct.getBottom());
    	
    	Animation anim = null;    
    	anim = new TranslateAnimation(0, ib5move, 0, 0);    
    	anim.setInterpolator(new DecelerateInterpolator());    
    	anim.setDuration(300);
    	
    	anim.setAnimationListener(new AnimationListener(){
    	    public void onAnimationEnd(Animation animation) {
    	    	ib5_ct.clearAnimation();
    	    	int gox = ((tabindex*106)+28);
    	    	int ib5left = (int)(gox*density);
    	    	
    	    	Log.v("tabmove", "已完成准备去" + gox + "dp," + ib5left + "px处");
    	    	
    	    	int top = ib5_ct.getTop();
    	    	int right = (int)ib5_ct.getWidth() + (int)ib5left;
    	    	int buttom = ib5_ct.getHeight()+ib5_ct.getTop();
    	    	
    	    	ib5_ct.layout(ib5left, top , right , buttom);
    	    	
    	    	Log.v("tabmove", "已完成，现在在" + ib5left + "," + top + "," + right + "," + buttom);
    	    	
    	    	Log.v("tabmove", "已完成，现在在" + ib5_ct.getLeft() + "," + ib5_ct.getTop() + "," + ib5_ct.getRight() + "," + ib5_ct.getBottom());
    	    }
    	    public void onAnimationRepeat(Animation animation) { }
    	    public void onAnimationStart(Animation animation) { }
    	});
    	//ib5_ct.setAnimation(anim);
    	
    	
    	
	}
	
	
	
    //退出确认  
    public boolean onKeyDown(int keyCode,KeyEvent event)   
    {   
    	
        if((keyCode==KeyEvent.KEYCODE_BACK))   
        {   
        	
        	new AlertDialog.Builder(this)
            .setIcon(R.drawable.icon)
            .setTitle(R.string.exit_title)
            .setMessage(R.string.exit_info)
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    	
                    }
            })
            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	toolImageCache.saveFileCache();
                    	
                        finish();
                    	//System.exit(0);
                    }
            }).show();
        	
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

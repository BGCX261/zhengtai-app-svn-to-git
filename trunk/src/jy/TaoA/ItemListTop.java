package jy.TaoA;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.taobao.api.domain.TaobaokeItem;
import com.umeng.analytics.MobclickAgent;

import jy.TaoA.R;
import jy.TaoA.models.dataSource;
import jy.TaoA.models.modelImageLoader;
import jy.TaoA.models.modelItem;
import jy.tools.toolBitmap;
import jy.tools.toolHttp;
import jy.tools.toolCommon;
import jy.tools.toolImageCache;
import jy.tools.toolImageLoaderLib;
import android.R.id;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;


public class ItemListTop extends Activity {
	
	public List<modelImageLoader> images = new ArrayList<modelImageLoader>();
	
	private List<TaobaokeItem> tbdata = new ArrayList<TaobaokeItem>();
	
	//测试
	private ListView lv1;
	private TaoKeListAdapter la;
	
	public ImageView test;
	public TextView test_title;
	
	//滚动条用
	private TextView ib5_ct ;
	float density= 0.0f;
	
	private int pagenum = 0;
	
	
	public String classid = "";
	public TextView top_title;
	
	private LinearLayout lv_footer;
	private LinearLayout lv_nextpage;
	private LinearLayout lv_loading;
	
	
	public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
 
        toolCommon.setContext(getApplicationContext());
        
        setContentView(R.layout.tab_itemlist);
        
       
        //初始化主界面
        initViews();
        
        String data = this.getIntent().getExtras().getString("classid");
        
        
       
        classid = data.split("-")[1];
        
        top_title.setText(data.split("-")[0]);
        

		

        //给listview条目添加事件
	    this.lv1.setOnItemClickListener(new ListView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

				if(pos < la.getCount()){
					
					TaobaokeItem mi = (TaobaokeItem) la.getItem(pos);
					
					String url = dataSource.getTtidUrl(mi.getClickUrl());
					
					//toolCommon.Msg(url);
					
					Intent intent = new Intent();
	                intent.setClass(ItemListTop.this, ViewInBrowser.class);
	                intent.putExtra("tourl", url);
	                startActivity(intent);
	                
				}
			}
		});
        
        
	    NextPage();
    }
	
	
	
	
	//分页读取数据
	public void NextPage(){
		
		lv_nextpage.setVisibility(View.GONE);
		lv_loading.setVisibility(View.VISIBLE);
		
		Thread thread = new Thread() {
			@Override
			public void run() {
				
				//读取数据
				//List<TaobaokeItem> data = dataSource.getItems(key,"",pagenum + 1);
				
				List<TaobaokeItem> data = dataSource.saeGetListData(classid);
				
				if(data != null){
					Message message = handlerTest.obtainMessage();
					message.obj = data;
					message.what = 1;
					pagenum = pagenum + 1;
					handlerTest.sendMessage(message);
				}else{
					Message message = handlerTest.obtainMessage();
					message.what = 2;
					handlerTest.sendMessage(message);
				}
			}
		};
		
		thread.start();
		thread = null;
	
	}
	
	//分页数据后续操作
	final Handler handlerTest = new Handler() {
		//@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			
            case 1: //有数据
                
				List<TaobaokeItem> newdata = (List<TaobaokeItem>)(msg.obj);
				
				for (int ii=0;ii<newdata.size();ii++){
					tbdata.add(newdata.get(ii));
				}
				
				la.notifyDataSetChanged();
				
				break;
				
            case 2: //无数据
            	toolCommon.Msg("读取数据不成功");
            	
            	break;
		    }
			
			//lv_nextpage.setVisibility(View.VISIBLE);
			lv_loading.setVisibility(View.GONE);
			
		}
	};
	
	
	
	
	
	//初始化页面控件
	public void initViews(){
		
        
		toolImageCache.loadFileCache();

		top_title = (TextView) this.findViewById(R.id.top_title);
        
        lv1 = (ListView) this.findViewById(R.id.listView1);
        lv_footer = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.vlist_next, null);
        lv_nextpage = (LinearLayout)lv_footer.findViewById(R.id.listnextpage);
        lv_loading = (LinearLayout)lv_footer.findViewById(R.id.listloading);
        this.lv1.addFooterView(lv_footer);
        la = new TaoKeListAdapter(this,tbdata);
        lv1.setAdapter(la);
        
        
        //给下一页按钮添加事件
        this.lv_nextpage.setOnClickListener(new LinearLayout.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				NextPage();
			}
        	
        });
        
        
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

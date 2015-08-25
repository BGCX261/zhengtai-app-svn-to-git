package jy.TaoA.models;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jy.TaoA.R;
import jy.tools.toolCommon;
import jy.tools.toolHttp;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.TaobaokeItem;
import com.taobao.api.request.TaobaokeItemsGetRequest;
import com.taobao.api.request.UserGetRequest;
import com.taobao.api.response.TaobaokeItemsGetResponse;
import com.taobao.api.response.UserGetResponse;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.util.Log;

public class dataSource {
	
	
	public static String AppKey = "12620842";
	
	public static String AppSecret = "03daed8c86cf0fe77fc7c89cde4317cc";
	
	public static String saeHost = "http://zhengtai.sinaapp.com/";
	
	
	public static List<TaobaokeItem> saeGetListData(String classid){
		
		List<TaobaokeItem> data = new ArrayList<TaobaokeItem>();
		
		String dataUrl = saeHost + "out_list.php?classid=" + classid;
		
		String re = toolHttp.Get(dataUrl);
		
		Log.v("sae", "sae"+dataUrl+re);
		
		JSONObject jsdata;
		
		try {
			jsdata = new JSONObject(re);
			JSONArray jr = jsdata.getJSONArray("datas");
			
			for ( int i = 0;i<jr.length();i++ ){
				
				JSONObject jo = (JSONObject) jr.get(i);
				TaobaokeItem tk = new TaobaokeItem();
				
				Log.v("sae", "sae"+jo.getString("title"));
				
				tk.setTitle(jo.getString("title"));
				tk.setPicUrl(jo.getString("picurl"));
				tk.setPrice(jo.getString("price"));
				tk.setClickUrl(jo.getString("clickurl"));
				tk.setShopClickUrl(jo.getString("subtitle"));
				
				data.add(tk);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return data;
	}
	
	
	public static List<TaobaokeItem> getItems(String keyword, String classid, int pagenum){
		
		List<TaobaokeItem> result = null;
		
		TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", AppKey, AppSecret);
		
		//TaobaoClient client = new DefaultTaobaoClient("http://gw.api.tbsandbox.com/router/rest", "12620842", "03daed8c86cf0fe77fc7c89cde4317cc");
		
		TaobaokeItemsGetRequest req=new TaobaokeItemsGetRequest();
		
		req.setFields("num_iid,title,nick,pic_url,price,click_url,commission,commission_rate,commission_num,commission_volume,shop_click_url,seller_credit_score,item_location,volume");
		req.setNick("fairy_ball");
		
		if(keyword != ""){
			req.setKeyword(keyword);
		}
		
		if(classid != ""){
			//
		}
		
		
		req.setStartCommissionRate("2000");
		req.setEndCommissionRate("9000");
		req.setPageSize((long)10);
		req.setPageNo((long)pagenum);
		
		
		try {
			TaobaokeItemsGetResponse response = client.execute(req);
			result = response.getTaobaokeItems();
			
		} catch (ApiException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return result;
	}
	
	
	
	public static String getTtidUrl(String url){
		
		String re = url;
		
		re = re + "&ttid=" + "400000_" + AppKey + "@wazt_Android_" + "0.1.0";
		
		re = re + "&sid=" + "xxxxxxxx";
		
		return re;
	}
	
}

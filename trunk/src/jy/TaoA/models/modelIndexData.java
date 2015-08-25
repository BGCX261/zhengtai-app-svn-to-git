package jy.TaoA.models;

import java.io.Serializable;

import com.taobao.api.domain.TaobaokeItem;

import android.graphics.Bitmap;

public class modelIndexData {
	
	public String title;
	public String classid;
	public String picurl;
	
	public modelIndexData(String t, String c, String p){
		title = t;
		classid = c;
		picurl = p;
	}
}

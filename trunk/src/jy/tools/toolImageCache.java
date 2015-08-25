package jy.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;


	//图片缓存类
	public class toolImageCache{
		
		
		//软引用缓存表
		private static HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
		
		//往软引用表中添加一行
		public static void addPic(String picname, SoftReference<Bitmap> pic){
			
			if(imageCache.containsKey(picname)){
				
			}else{
				imageCache.put(picname, pic);
				Log.v("filetest","jy.imagecache_已经添加到软引用表。。。"+imageCache.size());
			}
			
		}
		
		//从软引用表中获取图片
		public static Bitmap getPic(String picname){
			
			Bitmap re = null;
			
			SoftReference<Bitmap> pic = imageCache.get(picname);
			
			if(pic == null){
				Log.v("x", "jy.imagecache_soft列表不命中"+picname);
				return re;
			}
			
			Log.v("x", "jy.imagecache_soft列表命中"+picname);
			
			re = pic.get();
			
			if(re == null){
				Log.v("x", "jy.imagecache_soft对象没有命中"+picname);
				imageCache.remove(picname);
			}else{
				Log.v("x", "jy.imagecache_soft对象命中！"+picname);
			}
			
			return re;
		}
		
		
		
		
		
		
		//获取软件的缓存路径 TaoA
		public static File getCachePath(){
			
			File sdpath = Environment.getExternalStorageDirectory();
			
			File mypath = null;
			
			if(sdpath.canRead() && sdpath.canWrite()){
				
				mypath = new File(sdpath.getAbsolutePath()+"/TaoA");
				
				if(!mypath.exists()){
					mypath.mkdir();
	            }
				
			}
			
			return mypath;
		}
		
		
		
		
		
		
		//文件缓存表
		private static HashMap<String, Date> fileCache = new HashMap<String, Date>();
		
		private static int count = 0;
		
		public static void addFile(String filename, Bitmap bm, int cacheHours){
			
			//存到本地
			File pic = new File(toolImageCache.getCachePath() + "/" + filename);
			
			FileOutputStream outStream;
			
			try {
				outStream = new FileOutputStream(pic);
				bm = toolBitmap.GetThumb(bm, 225, 180);
				bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
				outStream.close();
				Log.v("filetest","jy.imageDown_已经写图片到本地。。。");
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			//顺便设置缓存时间
			Date d = toolCommon.getTime("", cacheHours);
			fileCache.put(filename,d);
			Log.v("filetest","jy.imageDown_已经添加到文件缓存表。。。"+d.toLocaleString());
			
		}
		
		
		public static void delFile(String filename){
			//未实现，貌似不用了，在每次载入时会清理
		}
		
		
		public static Bitmap getFile(String filename){
			
			Bitmap re = null;
			
			re = BitmapFactory.decodeFile(getCachePath()+ "/" +filename);
			
			return re;
		}
		
		
		//从磁盘上载入文件缓存数据
		public static void loadFileCache(){
			
			if(getCachePath() == null){
				
				//toolCommon.Msg("木有找到存储卡，暂不缓存图片");
				
				return;
			}
			
			
			File piclist = new File(getCachePath().getAbsolutePath() + "/cache.json");
			
			String picstr = "";
			
			
			Log.v("filetest","jy.loadfilecache_先读取数据缓存");
			//先读取数据缓存
			if(piclist.exists()){
				//如果有就载入文本
				picstr = toolCommon.fileRead(piclist);
				Log.v("filetest","jy.loadfilecache_有缓存文件，直接载入"+picstr);
			}else{
				//如果没有就添加cache.json
				toolCommon.fileWrite(piclist, "[]");
				picstr = "[]";
				Log.v("filetest","jy.loadfilecache_无缓存文件，新建空json文件并保存"+picstr);
			}
			
			
			
			HashMap<String, Date> temp = Json2Cache(picstr);
			
			String tempdata = "";//用来存储数据中文件名字符串的临时字段
			
			
			Log.v("filetest","jy.loadfilecache_准备删除已过期数据，缓存数据条数："+temp.size());
			
			//先删除已经过期的数据
			for (Iterator iter = temp.entrySet().iterator(); iter.hasNext();){
				
				Map.Entry entry = (Map.Entry) iter.next();
				Date extime = (Date)entry.getValue();
				
				Date now = toolCommon.getTime("",0);
				//如果现在已经超过了过期时间，就删除这行数据
				if(now.after(extime)){
					Log.v("filetest","jy.loadfilecache_遍历数据，已删除一项过期"+extime.toString()+"---now"+now.toString());
					iter.remove();
				}else{
					Log.v("filetest","jy.loadfilecache_遍历数据，一项未过期跳过"+extime.toLocaleString()+"---now"+now.toLocaleString());
					tempdata = tempdata + entry.getKey().toString();
				}
				
			}
			
			Log.v("filetest","jy.loadfilecache_准备删除有图但没有数据的图");
			//再删除有图但没有数据的图...
			File fld = getCachePath();
			File[] pics = fld.listFiles();
			
			String tempfile = "";
			//遍历图片
			for(int i = 0; i < pics.length; i++){
				String filename = pics[i].getName();
				//排除json文件
				if(filename.indexOf("json") < 0){
					//如果没能在数据缓存中找到，就把图片删了
					if(tempdata.indexOf(filename) < 0){
						Log.v("filetest","jy.loadfilecache_遍历图片，数据中没有找到，删除一个图片"+filename);
						pics[i].delete();
					}else{
						Log.v("filetest","jy.loadfilecache_遍历图片，数据中已找到，跳过图片"+filename);
						tempfile = tempfile + filename;
					}
				}
			}
			
			
			Log.v("filetest","jy.loadfilecache_再删除有数据，但没有图的，我操。。。");
			//再删除有数据，但没有图的，我操。。。
			for (Iterator iter = temp.entrySet().iterator(); iter.hasNext();){
				
				Map.Entry entry = (Map.Entry) iter.next();
				String filename = entry.getKey().toString();
				
				//如果数据对应不到本地图片，就删除
				if(tempfile.indexOf(filename) < 0){
					Log.v("filetest","jy.loadfilecache_数据中的图片本地没有，删除图片"+filename);
					File del = new File(getCachePath() + "/" + filename);
					del.delete();
				}
				else{
					Log.v("filetest","jy.loadfilecache_数据中的图片文件存在"+filename);
				}
				
			}
			
			Log.v("filetest","jy.loadfilecache_完工。。。");
			//完工。。。此时图片数据和缓存表完全对应，且不存在过期的图片。
			
			
			fileCache = temp;
			
			//保存最终的缓存文件到本地
			saveFileCache();
			
		}
		
		
		//在磁盘上保存文件缓存表的数据
		public static void saveFileCache(){
			
				if(getCachePath() == null){
				
				//toolCommon.Msg("木有找到存储卡，暂不缓存图片");
				
				return;
			}


			File piclist = new File(getCachePath().getAbsolutePath() + "/cache.json");
			
			String re = Cache2Json();
			
			toolCommon.fileWrite(piclist, re);
			
			Log.v("filetest","jy.savefilecache_保存了file文本。。。"+re);
		}
		
		
		//文件缓存数据序列化
		public static String Cache2Json(){
			
			String pics = "";
			
			JSONArray arr = new JSONArray();
			
			for (Iterator<Entry<String, Date>> iter = fileCache.entrySet().iterator(); iter.hasNext();){
				
				Map.Entry entry = (Map.Entry) iter.next();
				
				String url = entry.getKey().toString();
				Date extime1 = (Date)entry.getValue();
				
				String extime = toolCommon.getTimeStr(extime1);
				
				JSONObject jo = new JSONObject();
				
				try {
					jo.put("url", url);
					jo.put("extime", extime);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				arr.put(jo);
			}
			
			String re = arr.toString();
			
			//toolCommon.Msg(re);
			
			return re;
		}
		
		
		//文件缓存数据反序列化
		public static HashMap<String, Date> Json2Cache(String jsonstr){
			
			String data = jsonstr;
			
			HashMap<String, Date> re = new HashMap<String, Date>();
			
			try {
				JSONArray arr = new JSONArray(data);
				
				Log.v("db", "jy.json2cache_json数组行数"+arr.length());
				
				for (int i = 0; i < arr.length(); i++){
					
					JSONObject jo = (JSONObject) arr.get(i);
					
					String url = jo.getString("url");
					String extime = jo.getString("extime");
					
					
					re.put(url, toolCommon.getTime(extime, 0));
					
					Log.v("db", "jy.json2cache_json添加一行"+url+"-----"+extime);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			
			Log.v("db", "jy.json2cache_HashMap数组返回前行数"+re.size());
			
			return re;
		}
		
		
		
		
		
		//直接使用数据库方案，已废弃
		public static void cleanFileCache_bak(){
			
			File picpath = getCachePath();
			File[] pics = picpath.listFiles();
			
			
			//遍历本地图片
			String pics_sql = "";
			for (int i = 0;i<pics.length;i++){
				pics_sql = pics_sql + "'" + pics[i].getName()+ "'" + ",";
			}
			pics_sql = pics_sql + "'1248hfiidsfjpokdsa'";
			
			
			//清理数据库(step1.去除已经被删除的图片数据)
			pics_sql = "delete from pic_temp where url not in ("+ pics_sql +")";
			toolDB.execSQL(pics_sql);
			Log.v("db", "jy.db_step1_"+pics_sql);
			
			
			//清理数据库(step2.去除已经标记过期的图片数据和文件)
			//未完，数据库中没有图未处理
			pics_sql = "select * from pic_temp where extime > '" + toolCommon.getTime() +"'";
			Cursor cu = toolDB.Query(pics_sql, null);
			
			pics_sql = "";
			while(cu.moveToNext())
			{
				File f = new File(toolImageCache.getCachePath().getAbsolutePath() + "/" + cu.getString(cu.getColumnIndex("url")));
				
				if(f.exists()){f.delete();}
				
				pics_sql = pics_sql + "'" + cu.getString(cu.getColumnIndex("url"))+ "'" + ",";
			}
			pics_sql = pics_sql + "'1248hfiidsfjpokdsa'";
			cu.close();
			
			pics_sql = "delete from pic_temp where url in ("+ pics_sql +")";
			toolDB.execSQL(pics_sql);
			Log.v("db", "jy.db_step2_"+pics_sql);
			
			toolDB.closeDB();

			
		}
	}
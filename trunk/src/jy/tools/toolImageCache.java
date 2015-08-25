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


	//ͼƬ������
	public class toolImageCache{
		
		
		//�����û����
		private static HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
		
		//�������ñ������һ��
		public static void addPic(String picname, SoftReference<Bitmap> pic){
			
			if(imageCache.containsKey(picname)){
				
			}else{
				imageCache.put(picname, pic);
				Log.v("filetest","jy.imagecache_�Ѿ���ӵ������ñ�����"+imageCache.size());
			}
			
		}
		
		//�������ñ��л�ȡͼƬ
		public static Bitmap getPic(String picname){
			
			Bitmap re = null;
			
			SoftReference<Bitmap> pic = imageCache.get(picname);
			
			if(pic == null){
				Log.v("x", "jy.imagecache_soft�б�����"+picname);
				return re;
			}
			
			Log.v("x", "jy.imagecache_soft�б�����"+picname);
			
			re = pic.get();
			
			if(re == null){
				Log.v("x", "jy.imagecache_soft����û������"+picname);
				imageCache.remove(picname);
			}else{
				Log.v("x", "jy.imagecache_soft�������У�"+picname);
			}
			
			return re;
		}
		
		
		
		
		
		
		//��ȡ����Ļ���·�� TaoA
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
		
		
		
		
		
		
		//�ļ������
		private static HashMap<String, Date> fileCache = new HashMap<String, Date>();
		
		private static int count = 0;
		
		public static void addFile(String filename, Bitmap bm, int cacheHours){
			
			//�浽����
			File pic = new File(toolImageCache.getCachePath() + "/" + filename);
			
			FileOutputStream outStream;
			
			try {
				outStream = new FileOutputStream(pic);
				bm = toolBitmap.GetThumb(bm, 225, 180);
				bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
				outStream.close();
				Log.v("filetest","jy.imageDown_�Ѿ�дͼƬ�����ء�����");
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			//˳�����û���ʱ��
			Date d = toolCommon.getTime("", cacheHours);
			fileCache.put(filename,d);
			Log.v("filetest","jy.imageDown_�Ѿ���ӵ��ļ����������"+d.toLocaleString());
			
		}
		
		
		public static void delFile(String filename){
			//δʵ�֣�ò�Ʋ����ˣ���ÿ������ʱ������
		}
		
		
		public static Bitmap getFile(String filename){
			
			Bitmap re = null;
			
			re = BitmapFactory.decodeFile(getCachePath()+ "/" +filename);
			
			return re;
		}
		
		
		//�Ӵ����������ļ���������
		public static void loadFileCache(){
			
			if(getCachePath() == null){
				
				//toolCommon.Msg("ľ���ҵ��洢�����ݲ�����ͼƬ");
				
				return;
			}
			
			
			File piclist = new File(getCachePath().getAbsolutePath() + "/cache.json");
			
			String picstr = "";
			
			
			Log.v("filetest","jy.loadfilecache_�ȶ�ȡ���ݻ���");
			//�ȶ�ȡ���ݻ���
			if(piclist.exists()){
				//����о������ı�
				picstr = toolCommon.fileRead(piclist);
				Log.v("filetest","jy.loadfilecache_�л����ļ���ֱ������"+picstr);
			}else{
				//���û�о����cache.json
				toolCommon.fileWrite(piclist, "[]");
				picstr = "[]";
				Log.v("filetest","jy.loadfilecache_�޻����ļ����½���json�ļ�������"+picstr);
			}
			
			
			
			HashMap<String, Date> temp = Json2Cache(picstr);
			
			String tempdata = "";//�����洢�������ļ����ַ�������ʱ�ֶ�
			
			
			Log.v("filetest","jy.loadfilecache_׼��ɾ���ѹ������ݣ���������������"+temp.size());
			
			//��ɾ���Ѿ����ڵ�����
			for (Iterator iter = temp.entrySet().iterator(); iter.hasNext();){
				
				Map.Entry entry = (Map.Entry) iter.next();
				Date extime = (Date)entry.getValue();
				
				Date now = toolCommon.getTime("",0);
				//��������Ѿ������˹���ʱ�䣬��ɾ����������
				if(now.after(extime)){
					Log.v("filetest","jy.loadfilecache_�������ݣ���ɾ��һ�����"+extime.toString()+"---now"+now.toString());
					iter.remove();
				}else{
					Log.v("filetest","jy.loadfilecache_�������ݣ�һ��δ��������"+extime.toLocaleString()+"---now"+now.toLocaleString());
					tempdata = tempdata + entry.getKey().toString();
				}
				
			}
			
			Log.v("filetest","jy.loadfilecache_׼��ɾ����ͼ��û�����ݵ�ͼ");
			//��ɾ����ͼ��û�����ݵ�ͼ...
			File fld = getCachePath();
			File[] pics = fld.listFiles();
			
			String tempfile = "";
			//����ͼƬ
			for(int i = 0; i < pics.length; i++){
				String filename = pics[i].getName();
				//�ų�json�ļ�
				if(filename.indexOf("json") < 0){
					//���û�������ݻ������ҵ����Ͱ�ͼƬɾ��
					if(tempdata.indexOf(filename) < 0){
						Log.v("filetest","jy.loadfilecache_����ͼƬ��������û���ҵ���ɾ��һ��ͼƬ"+filename);
						pics[i].delete();
					}else{
						Log.v("filetest","jy.loadfilecache_����ͼƬ�����������ҵ�������ͼƬ"+filename);
						tempfile = tempfile + filename;
					}
				}
			}
			
			
			Log.v("filetest","jy.loadfilecache_��ɾ�������ݣ���û��ͼ�ģ��Ҳ١�����");
			//��ɾ�������ݣ���û��ͼ�ģ��Ҳ١�����
			for (Iterator iter = temp.entrySet().iterator(); iter.hasNext();){
				
				Map.Entry entry = (Map.Entry) iter.next();
				String filename = entry.getKey().toString();
				
				//������ݶ�Ӧ��������ͼƬ����ɾ��
				if(tempfile.indexOf(filename) < 0){
					Log.v("filetest","jy.loadfilecache_�����е�ͼƬ����û�У�ɾ��ͼƬ"+filename);
					File del = new File(getCachePath() + "/" + filename);
					del.delete();
				}
				else{
					Log.v("filetest","jy.loadfilecache_�����е�ͼƬ�ļ�����"+filename);
				}
				
			}
			
			Log.v("filetest","jy.loadfilecache_�깤������");
			//�깤��������ʱͼƬ���ݺͻ������ȫ��Ӧ���Ҳ����ڹ��ڵ�ͼƬ��
			
			
			fileCache = temp;
			
			//�������յĻ����ļ�������
			saveFileCache();
			
		}
		
		
		//�ڴ����ϱ����ļ�����������
		public static void saveFileCache(){
			
				if(getCachePath() == null){
				
				//toolCommon.Msg("ľ���ҵ��洢�����ݲ�����ͼƬ");
				
				return;
			}


			File piclist = new File(getCachePath().getAbsolutePath() + "/cache.json");
			
			String re = Cache2Json();
			
			toolCommon.fileWrite(piclist, re);
			
			Log.v("filetest","jy.savefilecache_������file�ı�������"+re);
		}
		
		
		//�ļ������������л�
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
		
		
		//�ļ��������ݷ����л�
		public static HashMap<String, Date> Json2Cache(String jsonstr){
			
			String data = jsonstr;
			
			HashMap<String, Date> re = new HashMap<String, Date>();
			
			try {
				JSONArray arr = new JSONArray(data);
				
				Log.v("db", "jy.json2cache_json��������"+arr.length());
				
				for (int i = 0; i < arr.length(); i++){
					
					JSONObject jo = (JSONObject) arr.get(i);
					
					String url = jo.getString("url");
					String extime = jo.getString("extime");
					
					
					re.put(url, toolCommon.getTime(extime, 0));
					
					Log.v("db", "jy.json2cache_json���һ��"+url+"-----"+extime);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			
			Log.v("db", "jy.json2cache_HashMap���鷵��ǰ����"+re.size());
			
			return re;
		}
		
		
		
		
		
		//ֱ��ʹ�����ݿⷽ�����ѷ���
		public static void cleanFileCache_bak(){
			
			File picpath = getCachePath();
			File[] pics = picpath.listFiles();
			
			
			//��������ͼƬ
			String pics_sql = "";
			for (int i = 0;i<pics.length;i++){
				pics_sql = pics_sql + "'" + pics[i].getName()+ "'" + ",";
			}
			pics_sql = pics_sql + "'1248hfiidsfjpokdsa'";
			
			
			//�������ݿ�(step1.ȥ���Ѿ���ɾ����ͼƬ����)
			pics_sql = "delete from pic_temp where url not in ("+ pics_sql +")";
			toolDB.execSQL(pics_sql);
			Log.v("db", "jy.db_step1_"+pics_sql);
			
			
			//�������ݿ�(step2.ȥ���Ѿ���ǹ��ڵ�ͼƬ���ݺ��ļ�)
			//δ�꣬���ݿ���û��ͼδ����
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
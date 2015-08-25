package jy.tools;

import java.io.File;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class toolDB {
	
	private static SQLiteDatabase db = null;
	
	public static void start(){
		int flag = 0;
		if((db == null || db.isOpen() == false) && flag == 0){
			flag = 1;
			//File mypath = new File(toolCommon.getContext().getFilesDir()+"/appnew.db");
			//Log.v("filetest",mypath.getAbsolutePath() + "ÓÐÄ¾ÓÐ" + mypath.exists()+"¶Á"+mypath.canRead()+mypath.canWrite());
			//db = SQLiteDatabase.openDatabase( toolCommon.getContext().getFilesDir()+"/app.db" , null, SQLiteDatabase.OPEN_READWRITE);
			
			db = new SQLiteDB(toolCommon.getContext(),null,null,1).getWritableDatabase();
			Log.v("filetest",db.isOpen()+" sqlite×´Ì¬");
			flag = 0;
		}
	}
	
	
	
	public static void execSQL(String sql){
		start();
		db.execSQL(sql);
		//db.close();
	}
	
	public static Cursor Query(String sql,String[] args){
		start();
		Cursor re = db.rawQuery(sql, args);
		//db.close();
		return re;
	}
	
	public static SQLiteDatabase getDB(){
		start();
		
		return db;
	}
	
	public static void closeDB(){
		if(db.isOpen()){
			db.close();
		}
	}
}

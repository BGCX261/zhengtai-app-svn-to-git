package jy.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class SQLiteDB extends SQLiteOpenHelper {

	public static final String DB_NAME = "app.db";
    public static final int VERSION = 1;
    
	public SQLiteDB(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DB_NAME, factory, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//db.execSQL("CREATE TABLE if not exists pic_temp (url varchar(200)  PRIMARY KEY NOT NULL, extime datetime  NOT NULL)");
		
		//db.execSQL("CREATE TABLE if not exists pic_temp (url varchar(10000)  PRIMARY KEY NOT NULL, extime datetime  NOT NULL)");
		
		Log.v("filetest","jy.½¨±ísqlite");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}

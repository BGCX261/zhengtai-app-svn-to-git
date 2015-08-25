package jy.TaoA.models;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import jy.TaoA.MainActivity;
import jy.TaoA.R;
import jy.TaoA.models.modelItem;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class modelImageLoader{
	
	public WeakReference<ImageView> iv;
	
	public String imgurl;
	
	public Bitmap bm;
	
	public modelImageLoader(WeakReference<ImageView> x,String y){
		iv = x;
		imgurl = y;
	}
	
}
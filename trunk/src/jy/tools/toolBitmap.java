package jy.tools;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

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
import android.util.Log;

public class toolBitmap {
	
	//缩放
	public static Bitmap GetThumb(Bitmap src,int toWidth,int toHeight){
		
		int srcWidth = src.getWidth();
		int srcHeight = src.getHeight();
		
		Matrix matrix = new Matrix();   
		// 计算缩放率，新尺寸除原始尺寸   
		float scaleWidth = ((float) toWidth) / srcWidth;   
		float scaleHeight = ((float) toHeight) / srcHeight;   

		matrix.postScale(scaleWidth, scaleHeight);
		
		src = Bitmap.createBitmap(src, 0, 0, srcWidth, srcHeight, matrix, true);   

		return src;
	}
	
	//剪切
	public static Bitmap GetCut(Bitmap src,int x,int y,int witdh,int height){
		
		return src;
	}
	
	//合并
	public static Bitmap GetPatch(Bitmap src,Bitmap patch,int x,int y){
		
		Bitmap bitmapIn = Bitmap.createBitmap(src.getWidth(),src.getHeight(),Config.ARGB_8888);
		
		Canvas canvasIn = new Canvas(bitmapIn);
		
		canvasIn.drawBitmap(src,0,0,null);		
		canvasIn.drawBitmap(patch, x, y, null);
		canvasIn.save(Canvas.ALL_SAVE_FLAG);
		canvasIn.restore();

		return bitmapIn;
	}
}

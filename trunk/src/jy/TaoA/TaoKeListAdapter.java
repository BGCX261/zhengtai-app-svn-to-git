package jy.TaoA;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.taobao.api.domain.TaobaokeItem;

import jy.TaoA.models.modelImageLoader;
import jy.TaoA.models.modelItem;
import jy.tools.toolImageLoaderLib;

import android.content.Context;
import android.os.Message;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;


public class TaoKeListAdapter extends BaseAdapter {
	
	private List<TaobaokeItem> items;
	Context context;
	
	public TaoKeListAdapter(Context context,List<TaobaokeItem> items){
		this.items = items;
		this.context = context;
	}
	

	@Override
	public int getCount() {
		return (items==null)?0:items.size();
	}

	
	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	
	public class ViewHolder{
		TextView itemTitle;
		TextView itemPrice;
		ImageView itemImage;
		TextView itemInfo;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final TaobaokeItem item = (TaobaokeItem)getItem(position);
		
		ViewHolder viewHolder = null;

//if(convertView == null){
			if(convertView == convertView){
				
				Log.w("ListAdapter", "新建convertView,position="+position);
				
				convertView = LayoutInflater.from(context).inflate(R.layout.vlist, null);
				
					viewHolder = new ViewHolder();
					
					viewHolder.itemTitle = (TextView)convertView.findViewById(R.id.title);
					viewHolder.itemPrice = (TextView)convertView.findViewById(R.id.price);
					viewHolder.itemInfo = (TextView)convertView.findViewById(R.id.info);
					viewHolder.itemImage = (ImageView)convertView.findViewById(R.id.img);

					convertView.setTag(viewHolder);
	
				
	/*			//动态增加1个ImageView
				viewHolder.imageView = new ImageView(context);
				LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				mParams.gravity = Gravity.CENTER;
				mParams.width=50;
				viewHolder.imageView.setLayoutParams(mParams);
				//这个ImageView放到ListView的第2列之后
				((LinearLayout)convertView).addView(viewHolder.imageView,2);
				*/
	
				
			}else{
				viewHolder = (ViewHolder)convertView.getTag();
				Log.w("ListAdapter", "旧的convertView,position="+position);
				
				viewHolder.itemImage.setBackgroundDrawable(null);
			}
		
  		//对ListView中第1个TextView配置OnClick事件
/*		viewHolder.textViewNext.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				final MainActivity activity = weak.get();
				activity.NextPage();
			}
		});*/

		
		
		String haha="";
		
		if(position+1==getCount()){
			haha="最后一个";
		}
		
		viewHolder.itemTitle.setText(String.valueOf(item.getTitle().replaceAll("\\<.+?\\>", "")));
		viewHolder.itemPrice.setText((position + 1) + "          ￥" + item.getPrice().replaceAll("\\.[0-9]+", ""));
		viewHolder.itemInfo.setText(item.getShopClickUrl());
		
		
		WeakReference img = new WeakReference((ImageView)convertView.findViewById(R.id.img));
		toolImageLoaderLib.getpic(new modelImageLoader(img,item.getPicUrl()));


		//Log.v("doInBack", "imgdown--收到了更新通知--");
		
		//if(item.image != null){
			//Log.v("doInBack", "imgdown--收到了更新通知，开始更新图片--");
			//viewHolder.itemImage.setImageBitmap(item.image);
		//}
		
		
		//对ListView中的每一行信息配置OnClick事件
/*		convertView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
			}
		});*/
		
		
		//对ListView中的每一行信息配置OnLongClick事件
/*		convertView.setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View v) {
				Toast.makeText(context, 
						"[convertView.setOnLongClickListener]点击了"+person.title, 
						Toast.LENGTH_SHORT).show();
				return true;
			}
		});*/
		
		return convertView;
		
	}

	
	
}

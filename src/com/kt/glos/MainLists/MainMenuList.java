package com.kt.glos.MainLists;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kt.glos.BaseActivity;
import com.kt.glos.DetailList;
import com.kt.glos.R;
import com.kt.glos.adapter.CustomAdapter;
import com.kt.glos.adapter.InterAdapterView;
import com.kt.glos.network.JASONNetWork;
import com.kt.glos.network.NetWorkComplete;
import com.kt.glos.network.UrlList;
import com.kt.glos.util.LogTag;

public class MainMenuList {
	
	ListView listView;
	ArrayList<InterAdapterView> dataList;
	CustomAdapter mAdapter;
	LayoutInflater inflater;
	
	long clickTime =0;
	boolean slideRight = false;
	Activity mActivity; 
	RelativeLayout addLayer;
	View listLayer;
	public MainMenuList(Activity mActivity, RelativeLayout addLayer)
	{
		this.mActivity = mActivity;
		this.addLayer = addLayer;
	}
	public void setView()
	{
		inflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		listLayer = inflater.inflate(((BaseActivity)mActivity).setScreenContentView(R.layout.main_sub_list), null);
		
		dataList = new ArrayList<InterAdapterView>();
		listView = (ListView)listLayer.findViewById(R.id.listView);
		mAdapter = new CustomAdapter(mActivity,((BaseActivity)mActivity).setScreenContentView(R.layout.main_row), dataList);
		listView.setAdapter(mAdapter);
		
		listPacket();
	}
	public void addView()
	{
		if(addLayer.getChildCount()>0)		addLayer.removeAllViews();
		addLayer.addView(listLayer);
	}
	public void listPacket() {
		ArrayList<String> nameList = new ArrayList<String>();
		ArrayList<String> postList = new ArrayList<String>();

		nameList.add(UrlList.limit);
		postList.add("20");

		JASONNetWork mJASONNetWork = new JASONNetWork(mActivity, nameList, postList, UrlList.MainList,UrlList.GET);
		mJASONNetWork.setNetWorkComplete(new NetWorkComplete() {

			@Override
			public void onNetWorkComplete(boolean isSucess, String jsonData)  
			{
				// TODO Auto-generated method stub
				if(isSucess)
				{
					try 
					{
						JSONObject order = new JSONObject(jsonData);
						JSONArray mArray = new JSONArray(order.getString("entities"));
						for(int i=0;i<mArray.length();i++)
						{
							dataList.add(new ListRow(((JSONObject)mArray.get(i)).toString()));
						}
						mAdapter.notifyDataSetChanged();
					} 
					catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						if(e != null)e.printStackTrace();
						Toast.makeText(mActivity, "통신이불안정합니다.", 1000).show();
					} 
				}
				else
				{
					Toast.makeText(mActivity, "통신이불안정합니다.", 1000).show();
				}
			}
		});
		mJASONNetWork.setProgress(true);
		mJASONNetWork.onTimeConnect(3,10000);
	}
	public class ListRow implements InterAdapterView
	{
		View view;
		String mJsonData;
		ImageView img;
		String imagePath;
		String masterId;
		public ListRow(String jsonData)
		{
			mJsonData = jsonData;
			view = inflater.inflate(((BaseActivity)mActivity).setScreenContentView(R.layout.main_row), null);
			setContent();
			view.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if(event.getAction()==MotionEvent.ACTION_DOWN)
					{
						if(System.currentTimeMillis()-clickTime<500 || slideRight) return false;
						clickTime = System.currentTimeMillis();
						return true;
					}
					else if(event.getAction()==MotionEvent.ACTION_UP)
					{
						clickTime = System.currentTimeMillis();
						LogTag.d("onClick");
						detailPacket(masterId,imagePath);
						listView.smoothScrollBy(0, 0);
						return false;
					}
					else 
						return false;
				}
			});
		}
		@Override
		public View getMyView() {
			// TODO Auto-generated method stub
			if(img!= null && img.getTag() == null && imagePath != null && imagePath.length()>0){
				((BaseActivity)mActivity).getUrlImage(img,imagePath);
				img.setTag("imageReady");
				LogTag.d("Glos","getMyView");
			}

			return view;
		}

		public void setContent()
		{
			try 
			{
				JSONObject order = new JSONObject(mJsonData);
				if(order.has("THUMB")){
					img = (ImageView) view.findViewById(R.id.img);
					imagePath = order.getString("THUMB");
				}
				if(order.has("NAME")){
					TextView text01 = (TextView) view.findViewById(R.id.text01);
					text01.setText(order.getString("NAME"));
					text01.setClickable(false);
				}
				if(order.has("SCORE")){
					TextView text02 = (TextView) view.findViewById(R.id.text02);
					text02.setText(getscore(order.getInt("SCORE")));				
				}
				if(order.has("MASTERID")){
					masterId = 	order.getString("MASTERID");		
				}
			} 
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@Override
		public void setEvent() {}
		public String getscore(int value)
		{
			String score = null;
			for(int i=0;i<value;i++)
			{
				if(score== null) score= "★";
				else score = score+"★";
			}
			return score;
		}
	}
	public void detailPacket(String masterId,final String imagePath)
	{

		ArrayList<String> nameList = new ArrayList<String>();
		ArrayList<String> postList = new ArrayList<String>();

		nameList.add("ql");
		postList.add("select * where MASTERID=" +"'"+masterId+"'");

		JASONNetWork mJASONNetWork = new JASONNetWork(mActivity, nameList, postList, UrlList.DetailList,UrlList.GET);
		mJASONNetWork.setNetWorkComplete(new NetWorkComplete() {

			@Override
			public void onNetWorkComplete(boolean isSucess, String jsonData)  
			{
				// TODO Auto-generated method stub
				if(isSucess)
				{
					Intent mIntent = new Intent(mActivity,DetailList.class);
					mIntent.putExtra("JsonData", jsonData);
					mIntent.putExtra("ImagePath", imagePath);
					mActivity.startActivity(mIntent);
					mActivity.overridePendingTransition(R.anim.action_in_up, R.anim.action_in_down);
				}
				else
				{
					Toast.makeText(mActivity, "통신이불안정합니다.", 1000).show();
				}
			}
		});
		mJASONNetWork.setProgress(true);
		mJASONNetWork.onTimeConnect(3,10000);
	}
	

}

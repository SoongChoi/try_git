package com.kt.glos.MainLists;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kt.glos.BaseActivity;
import com.kt.glos.R;
import com.kt.glos.data.UserItem;

public class MainMenuProfile {

	LayoutInflater inflater;
	boolean slideRight = false;
	Activity mActivity; 
	RelativeLayout addLayer;
	View profileLayer;
	UserItem userItem;
	ImageView img;
	public MainMenuProfile(Activity mActivity, RelativeLayout addLayer)
	{
		this.mActivity = mActivity;
		this.addLayer = addLayer;
		
		userItem = ((BaseActivity)mActivity).mApplication.getUserItem();
	}
	public void setView()
	{
		inflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		profileLayer = inflater.inflate(((BaseActivity)mActivity).setScreenContentView(R.layout.main_sub_profile), null);
		
		TextView text01 = (TextView)profileLayer.findViewById(R.id.text01);
		text01.setText(userItem.getUserDetailItem().getUsername());
		
		TextView text02 = (TextView)profileLayer.findViewById(R.id.text02);
		text02.setText(userItem.getUserDetailItem().getEmail());
		
		TextView text03 = (TextView)profileLayer.findViewById(R.id.text03);
		text03.setText(userItem.getUserDetailItem().getActivated());
		
		img = (ImageView)profileLayer.findViewById(R.id.img);
		((BaseActivity)mActivity).getUrlImage(img,userItem.getUserDetailItem().getPicture());
	}
	public void addView()
	{
		if(addLayer.getChildCount()>0)		addLayer.removeAllViews();
		addLayer.addView(profileLayer);
	}
}

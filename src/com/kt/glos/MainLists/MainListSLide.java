package com.kt.glos.MainLists;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kt.glos.BaseActivity;
import com.kt.glos.R;
import com.kt.glos.Search;
import com.kt.glos.util.LogTag;

public class MainListSLide extends BaseActivity {

	boolean slideRight = false;
	LinearLayout listLayer,menuLayer;
	Button menuBtn, searchBtn;
	RelativeLayout addLayer;
	MainMenuList mainMenuList;
	MainMenuProfile mainMenuProfile;
	int rightMenu=1;
	long clickTime =0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setMyContentView(R.layout.main_slide);
		listLayer = (LinearLayout)findViewById(R.id.listLayer);
		menuLayer = (LinearLayout)findViewById(R.id.menuLayer);
		addLayer = (RelativeLayout)findViewById(R.id.addLayer);

		
		 ((Button)findViewById(R.id.listBtn)).setOnClickListener(this);
		 ((Button)findViewById(R.id.profileBtn)).setOnClickListener(this);
		
		
		menuBtn = (Button)findViewById(R.id.menuBtn);
		menuBtn.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN)
				{
					if(System.currentTimeMillis()-clickTime<600) return false;
					clickTime = System.currentTimeMillis();
					return true;
				}
				else if(event.getAction()==MotionEvent.ACTION_UP)
				{
					setAnimation();
					return false;
				}
				else 
					return false;
			}
		});
		
		searchBtn = (Button)findViewById(R.id.searchBtn);
		searchBtn.setOnClickListener(this);
		
		mainMenuList = new MainMenuList(this,addLayer);
		mainMenuList.setView();
		mainMenuList.addView();
		
		mainMenuProfile =new MainMenuProfile(this,addLayer);
		mainMenuProfile.setView();
	}
	
	public boolean onBaseActivityClick(View v) {
		switch(v.getId()) {
		
		case R.id.listBtn:
			if(rightMenu==1) return true;
			mainMenuList.addView();
			rightMenu=1;
			setAnimation();
			break;
		case R.id.profileBtn:
			if(rightMenu==2) return true;
			mainMenuProfile.addView();
			rightMenu=2;
			setAnimation();
			break;

		case R.id.searchBtn:
			Intent intent = new Intent(getBaseContext(),Search.class);
			startActivity(intent);
			overridePendingTransition(R.anim.action_from_left, R.anim.action_to_right);
			break;
		}
		return true;
	}
	
	public void setAnimation()
	{
		listLayer.startAnimation(getSideAnimation());
	}
	
	public Animation getSideAnimation()
	{
		slideRight = (slideRight  ? false:true);
		
		mainMenuList.slideRight =slideRight;
		mainMenuProfile.slideRight =slideRight; 
		
		final int movex = (int) (pxWidth*0.8);
		TranslateAnimation ani;
		if(slideRight) ani = new TranslateAnimation(0, movex , 0, 0);
		else ani = new TranslateAnimation(0, -movex , 0, 0);
		ani.setDuration(500);
		ani.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				if(slideRight)menuLayer.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				LayoutParams mParam = new LayoutParams(pxWidth,pxHeight,Gravity.LEFT|Gravity.TOP);
				mParam.leftMargin= (slideRight  ? movex:0);
				listLayer.setLayoutParams(mParam);
				listLayer.clearAnimation();
				if(!slideRight)menuLayer.setVisibility(View.GONE);
			}
		});
		LogTag.d(slideRight+"");
		return ani;
		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(slideRight){
			setAnimation();
			return;
		}
		super.onBackPressed();
	}

}

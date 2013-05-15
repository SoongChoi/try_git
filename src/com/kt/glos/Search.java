package com.kt.glos;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

public class Search extends BaseActivity {
	AutoCompleteTextView searchInput;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setView();
	}
	
	public void setView() {
		setMyContentView(R.layout.search);
		
		searchInput = (AutoCompleteTextView)findViewById(R.id.searchInput);
		
		((ImageView)findViewById(R.id.searchBtn)).setOnClickListener(this);
		
	}

	@Override
	public boolean onBaseActivityClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.searchBtn:
			
			break;
		
		}
		return true;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		overridePendingTransition(R.anim.action_from_right, R.anim.action_to_left);
		
	}
}

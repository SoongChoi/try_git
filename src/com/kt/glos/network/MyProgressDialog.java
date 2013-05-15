package com.kt.glos.network;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.kt.glos.R;

public class MyProgressDialog extends Dialog{

	Activity activity;
	public MyProgressDialog(Activity activity) {
		super(activity,R.style.NewDialog);
		setCancelable(false);
		this.activity = activity;
	} 
	public void show(String message) {
		LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_myprogress, null);
		TextView mTextView = (TextView) layout.findViewById(R.id.text);
		mTextView.setText(message);
		addContentView(layout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		show();       
	}
}

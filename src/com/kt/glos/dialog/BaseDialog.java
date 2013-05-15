package com.kt.glos.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.kt.glos.BaseActivity;

public class BaseDialog implements OnClickListener{

	Dialog dialog;
	Activity activity;
	Context context;
	InterDialogResult mInterDialogResult;

	public BaseDialog(Activity activity)
	{
		dialog = new Dialog(activity);
		this.activity = activity;
	}
	protected void setMyContentView(int layoutId)
	{
		int changeName = ((BaseActivity)activity).setScreenContentView(layoutId);
		dialog.setContentView(changeName);
	}
	public void setInterDialogResult(InterDialogResult mInterDialogResult)
	{
		this.mInterDialogResult = mInterDialogResult;
	}
	public void show()
	{
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	}
	private boolean clickable = true;

	@Override
	public void onClick(View v) {
		v.setClickable(false);
		if (clickable) {
			clickable = false;
			if(onBaseDialogClick(v)){
				clickReset();
			}
		}
		v.setClickable(true);
	}

	public void clickReset() {
		clickable = true;
	}

	public boolean onBaseDialogClick(View v) {
		return true;
	}
}

package com.kt.glos.alert;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

import com.kt.glos.dialog.InterDialogResult;
import com.kt.glos.util.CommonConstant;

public class Dialog_Basic_TwoButton {

	Activity activity;
	InterDialogResult dialogResult;
	AlertDialog ad;
	AlertDialog.Builder ab;
	InputMethodManager imm;
	String resultString;
	String data;

	public Dialog_Basic_TwoButton(Activity activity, final InterDialogResult dialogResult) {
		this.activity = activity;
		ab = new AlertDialog.Builder(activity);
		this.dialogResult = dialogResult;
	}

	public void show(String title, String msg, boolean isReturn) {

		ab.setTitle(title);
		ab.setMessage(msg);
		ab.setPositiveButton("확인",new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				resultString = "ok";
			}
		});
		ab.setNegativeButton("취소", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				resultString = "cancel";
			}
		});
		ab.setCancelable(false);
		ab.create();

		ad = ab.show(); 
		ad.show();
		resultString = "return";
		if(isReturn)
		{
			ad.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					dialogResult.resultdialogObject(resultString, CommonConstant.RETURN_STRING);
				}
			});
		}

		ad.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if(keyCode == KeyEvent.KEYCODE_BACK)
				{
					resultString = "cancel";
					dialog.dismiss();
				}
				return false;
			}
		});
	}
}

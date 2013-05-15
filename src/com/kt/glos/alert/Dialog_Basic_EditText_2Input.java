package com.kt.glos.alert;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.kt.glos.R;
import com.kt.glos.dialog.InterDialogResult;
import com.kt.glos.util.CommonConstant;

public class Dialog_Basic_EditText_2Input {

	Activity activity;
	InterDialogResult dialogResult;
	AlertDialog ad;
	AlertDialog.Builder ab;
	InputMethodManager imm;
	String[] data = new String[3];

	public Dialog_Basic_EditText_2Input(Activity activity, final InterDialogResult dialogResult) {
		this.activity = activity;
		ab = new AlertDialog.Builder(activity);
		this.dialogResult = dialogResult;
	}

	public void show(String title, String msg, String hint1, String hint2) {
		LayoutInflater inflater = (LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_edittext2, null);
		final EditText text01 = (EditText)layout.findViewById(R.id.text01);
		final EditText text02 = (EditText)layout.findViewById(R.id.text02);

		text01.setText("chsoong");
		text02.setText("test1234");

		ab.setView(layout);
		ab.setTitle(title);
		ab.setMessage(msg);
		ab.setPositiveButton("확인", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				//통신설정 후 전화번호 가져가기
				data[1] = text01.getText().toString();
				data[2] = text02.getText().toString();
				imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(text01.getWindowToken(), 0);
				data[0] = "ok";
			}
		});
		ab.setNegativeButton("취소", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(text01.getWindowToken(), 0);
				data[0] = "cancel";
			}
		});
		ab.setCancelable(false);
		ab.create();

		ad = ab.show(); 
		text01.setHint(hint1);
		text02.setHint(hint2);
		ad.show();
		ad.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				if(!data[0].equals("ok"))
				{
					data[1] = "";
					data[2] = "";
				}
				dialogResult.resultdialogObject(data, CommonConstant.RETURN_STRINGs);
			}
		});
	}

}

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

public class Dialog_Basic_EditText_1Input {

	Activity activity;
	InterDialogResult dialogResult;
	AlertDialog ad;
	AlertDialog.Builder ab;
	InputMethodManager imm;
	String resultString;
	String data;

	public Dialog_Basic_EditText_1Input(Activity activity, final InterDialogResult dialogResult) {
		this.activity = activity;
		ab = new AlertDialog.Builder(activity);
		this.dialogResult = dialogResult;
	}

	public void show(String title, String msg, String hint) {
		LayoutInflater inflater = (LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_edittext1, null);
		final EditText findTxt = (EditText)layout.findViewById(R.id.findText);

		ab.setView(layout);
		ab.setTitle(title);
		ab.setMessage(msg);
		ab.setPositiveButton("확인", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				//통신설정 후 전화번호 가져가기
				data = findTxt.getText().toString();
				imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(findTxt.getWindowToken(), 0);
				resultString = "ok";
			}
		});
		ab.setNegativeButton("취소", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(findTxt.getWindowToken(), 0);
				resultString = "cancel";
			}
		});
		ab.setCancelable(false);
		ab.create();

		ad = ab.show(); 
		findTxt.setHint(hint);
		ad.show();
		ad.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				String[] result = null;
				if(resultString.equals("ok"))
				{
					result = new String[2];
					result[0] = resultString;
					result[1] = data;
				}
				else
				{
					result = new String[1];
					result[0] = resultString;
				}
				dialogResult.resultdialogObject(result, CommonConstant.RETURN_STRINGs);
			}
		});
	}

}

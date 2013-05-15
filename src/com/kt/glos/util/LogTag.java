package com.kt.glos.util;

import android.util.Log;

public class LogTag {

	private static Boolean isDebug = true;
	private static String TAG = "GLOS";
	static public void d(String msg) {
		if (isDebug) {
			Log.d(TAG, msg);
		}
	}
	static public void d(String tag,String msg) {
		if (isDebug) {
			Log.d(tag, msg);
		}
	}
	
	static public void e(String msg) {
		if (isDebug) {
			Log.e(TAG, msg);
		}
	}
	static public void e(String tag,String msg) {
		if (isDebug) {
			Log.e(tag, msg);
		}
	}
	
	static public void i(String msg) {
		if (isDebug) {
			Log.i(TAG, msg);
		}
	}
	static public void i(String tag,String msg) {
		if (isDebug) {
			Log.i(tag, msg);
		}
	}
	
	
}

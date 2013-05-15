package com.kt.glos.dialog;

public interface InterDialogResult {
	/**
	 * <strong>* Dialog returnData *</strong><br>
	 * <br>
	 * returnType은 int, int[], String, String[], class, Object등<br>
	 * 보내는곳과 받는곳에서 구분해서 데이터 전송할 것
	 * */
	public void resultdialogObject(Object obj, String returnType);
}

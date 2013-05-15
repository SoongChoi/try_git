package com.kt.glos;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.kt.glos.data.FaceBookItem;
import com.kt.glos.data.UserItem;
import com.kt.glos.facebook.MyFaceBook;
import com.kt.glos.network.NetWorkWatchDog;
/**
 * Application 클래스 재구현 전역 관련 변수 및 작업<br>
 * <br>
 * <li>생성자: usuhan</li>
 * <li>생성일: 2013. 4. 9.</li>
 * <li>최종 수정자: $Author: usuhan $</li>
 * <li>최종 수정일: $Date: 2013-04-09 12:00:00 +0900 (화, 9 4 2013) $</li>
 */
public class GlosApplication extends Application {

	MyFaceBook faceBook;
	UserItem userItem;
	FaceBookItem facebookItem;
	public boolean networkEnable=false;
	NetWorkWatchDog mNetWorkWatchDog;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		mNetWorkWatchDog = new NetWorkWatchDog();
		registerReceiver(mNetWorkWatchDog, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

		faceBook  = new MyFaceBook();
		faceBook.init(this);
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		unregisterReceiver(mNetWorkWatchDog);
		super.onTerminate();
	}

	public MyFaceBook getFaceBook() {
		if(faceBook == null) {
			faceBook  = new MyFaceBook();
			faceBook.init(this);
		}
		return faceBook;
	}

	public UserItem getUserItem() {
		return userItem;
	}

	public void setUserItem(UserItem userItem) {
		this.userItem = userItem;
	}

	public FaceBookItem getFacebookItem() {
		return facebookItem;
	}

	public void setFacebookItem(FaceBookItem facebookItem) {
		this.facebookItem = facebookItem;
	}

}

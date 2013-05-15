package com.kt.glos.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.kt.glos.GlosApplication;

public class NetWorkWatchDog extends BroadcastReceiver{
	GlosApplication mApplication = null;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		mApplication = (GlosApplication)context.getApplicationContext();
		String action = intent.getAction();
		
		if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION))
		{
			mApplication.networkEnable =  isNetWorkState(context);
			Toast.makeText(context, "web state chage "+mApplication.networkEnable, Toast.LENGTH_SHORT).show();
		}
	}
	
	public boolean isNetWorkState(Context context){

		ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(!mobile.isConnected()&&!wifi.isConnected())
		{
			return false;
		}
		else
		{
			return true;
		}
	}

}

package com.kt.glos.twitter;

import twitter4j.Twitter;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.kt.glos.network.MyProgressDialog;
import com.kt.glos.network.NetWorkComplete;
import com.kt.glos.util.LogTag;


public class RequestTockenAsysncTask {
	

	MyProgressDialog mProgressDialog;
	Handler mNetWorkHandler;
	NetWorkComplete mNetWorkComplete;
	Activity mActivity;
	boolean isShow=false;
	Twitter mTwitter;
	RequestToken mRqToken;
	TockenComplete mTockenComplete;
	AccessToken mAccessToken;
	String oauthVerifier;
	public interface TockenComplete{
		public void onTockenComplete(boolean isSuccess,Object mObject);
	}
	
	public void setTockenComplete(TockenComplete mTockenComplete){
		this.mTockenComplete = mTockenComplete;
	}

	//TODO RequestTockenAsysncTask
	public RequestTockenAsysncTask(Activity activity,Twitter mTwitter){
		this.mActivity = activity;
		this.mTwitter = mTwitter;
		mNetWorkHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(mProgressDialog != null)
				{
					mProgressDialog.dismiss();
				}
				if(msg.what==-1)
				{
					if(mTockenComplete != null)
					{
						mTockenComplete.onTockenComplete(false, null);
					}
				}
				if(msg.what==1)
				{
					if(mTockenComplete != null)
					{
						mTockenComplete.onTockenComplete(true, mRqToken);
					}
				}
				if(msg.what==2)
				{
					if(mTockenComplete != null)
					{
						mTockenComplete.onTockenComplete(true, mAccessToken);
					}
				}
			}

		};
	}

	public void setProgress(boolean isShow)
	{
		this.isShow = isShow;
	}

	public void onCallBackConnect()
	{
		if(!isShow){
			mProgressDialog = null;
		}
		else{
			mProgressDialog = new MyProgressDialog(mActivity);
			mProgressDialog.show("잠시만 기다려 주세요.");
		}

		CallBackThread mCallBackThread = new CallBackThread();
		mCallBackThread.start();
	}
	
	public void onOAuthConnect(RequestToken mRqToken,String oauthVerifier)
	{
		if(!isShow){
			mProgressDialog = null;
		}
		else{
			mProgressDialog = new MyProgressDialog(mActivity);
			mProgressDialog.show("잠시만 기다려 주세요.");
		}
		this.mRqToken = mRqToken;
		this.oauthVerifier = oauthVerifier;

		OAuthThread mOAuthThread = new OAuthThread();
		mOAuthThread.start();
	}

	public class CallBackThread extends Thread
	{
		public void run()
		{
			try 
			{
				mRqToken = mTwitter.getOAuthRequestToken(TwitterConstant.TWITTER_CALLBACK_URL);
				mNetWorkHandler.sendEmptyMessage(1);
			}
			catch (Exception ex) //TODO
			{
				LogTag.e("kim", ex.toString());
				mNetWorkHandler.sendEmptyMessage(-1);
			}
		}
	}
	
	public class OAuthThread extends Thread
	{
		public void run()
		{
			try 
			{
				mAccessToken =
						mTwitter.getOAuthAccessToken(mRqToken, oauthVerifier);
				mNetWorkHandler.sendEmptyMessage(2);
			}
			catch (Exception ex) //TODO
			{
				LogTag.e("kim", ex.toString());
				mNetWorkHandler.sendEmptyMessage(-1);
			}
		}
	}

}

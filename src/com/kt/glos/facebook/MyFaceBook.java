package com.kt.glos.facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.kt.glos.facebook.Facebook.DialogListener;
import com.kt.glos.network.UrlList;

public class MyFaceBook {


	public Facebook mFacebook;
	private AsyncFacebookRunner mAsyncRunner;

	public static String USER_ID = "";

	ProgressDialog dialog;	



	Activity mActivity;

	private static final String[] PERMISSIONS = 	new String[] {
		"email", "user_about_me", "friends_about_me", "user_activities", "friends_activities",
		"offline_access", "user_status,user_photos", "read_friendlists", "publish_actions",
		"publish_stream"
	};


	private boolean mbList = false;
	private boolean mbFeed = false;


	private BaseRequestListener mRequestListner = null;
	private String mTo;
	private String mName;
	private String mCaption;
	private String mMessage;
	private String mPicture;

	public void init(Context context){

		mFacebook = new Facebook(UrlList.GLOS_FACEBOOK_API_KEY);

		mAsyncRunner = new AsyncFacebookRunner(mFacebook);		

		SessionStore.restore(mFacebook, context);
	}

	public void setActivity(Activity activity){
		mActivity = activity;
	}
	public boolean isSessionValid(){
		return mFacebook.isSessionValid();
	}

	public void getRequestFaceBook(BaseRequestListener requestListner)
	{

		if(requestListner == null){
			mAsyncRunner.request("me", new SampleRequestListener());			

		}else{
			mAsyncRunner.request("me", requestListner);			
		}
	}

	/* 
	 * 리퀘스트 리스너
	 *  
	 */

	public class SampleRequestListener extends BaseRequestListener {

		public void onComplete(final String response, final Object state) {
			try {


				JSONObject json = Util.parseJson(response);   

				USER_ID = json.getString("id");


			} catch (JSONException e) {
				Log.w("Facebook-Example", "JSON Error in response");
			} catch (FacebookError e) {
				Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
			}
		}
	}
	//*******************************************************************************************************
	/*
	 * 로그인
	 */

	public void login(DialogListener dialogListener){
		if(dialogListener == null){
			mFacebook.authorize(mActivity, PERMISSIONS,
					new TestLoginListener());

		}else{
			mFacebook.authorize(mActivity, PERMISSIONS,
					dialogListener);
		}
	}

	public class TestLoginListener implements DialogListener {

		public void onComplete(Bundle values) {
			if (testAuthenticatedApi()) {
				SessionStore.save(mFacebook, mActivity);
				if(mbList){
					getFriendList(mRequestListner);
				}else if(mbFeed){
					feedSend(mTo, mName, mCaption, mMessage, mPicture, mRequestListner);
				}
			} else {
				if(mbList || mbFeed){
					mRequestListner.onFacebookError(new FacebookError("login fail"), null);
				}
			}
		}

		public void onCancel() {
			if(mbList || mbFeed){
				mRequestListner.onFacebookError(new FacebookError("login cancel"), null);
			}
		}

		public void onError(DialogError e) {
			e.printStackTrace();
		}

		public void onFacebookError(FacebookError e) {
			e.printStackTrace();
		}
	}

	public boolean testAuthenticatedApi() {
		if (!mFacebook.isSessionValid()) return false;
		try {
			Log.d("Tests", "Testing request for 'me'");
			String response = mFacebook.request("me");
			JSONObject obj = Util.parseJson(response);
			if (obj.getString("name") == null || 
					obj.getString("name").equals("")) {
				return false;
			}else{
				USER_ID = obj.getString("id");
			}

			Log.d("Tests", "All Authenticated Tests Passed");
			return true;
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
	}
	//*******************************************************************************************************

	//*******************************************************************************************************
	/*
	 * 로그 아웃
	 */

	public void logout(){
		runTestLogout();
	}

	public void runTestLogout() {
		if (testLogout()) {
			Log.d("Tests", "Testing logout : OK");
			SessionStore.clear(mActivity);
			USER_ID = "";
		} else {
			Log.d("Tests", "Testing logout : FAIL");
			SessionStore.clear(mActivity);
			USER_ID = "";
		}
	}


	public boolean testLogout() {
		try {
			Log.d("Tests", "Testing logout");
			String response = mFacebook.logout(mActivity);
			Log.d("Tests", "Got logout response: *" + response + "*");
			if (!response.equals("true")) {
				return false;
			}

			Log.d("Tests", "Testing logout on logged out facebook session");
			try {
				Util.parseJson(mFacebook.logout(mActivity));
				return false;
			} catch (FacebookError e) {
				if (e.getErrorCode() != 101 || 
						!e.getMessage().equals("Invalid API key") ) {
					return false;
				}
			}

			Log.d("Tests", "Testing logout on unauthenticated object");
			try {
				Util.parseJson(new Facebook(UrlList.GLOS_FACEBOOK_API_KEY).logout(mActivity));
				return false;
			} catch (FacebookError e) {
				if (e.getErrorCode() != 101 || 
						!e.getMessage().equals("Invalid API key") ) {
					return false;
				}
			}

			Log.d("Tests", "All Logout Tests Passed");
			return true;
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
	}
	//*******************************************************************************************************

	//*******************************************************************************************************
	/*
	 * 친구 목록
	 */
	public void getFriendList(BaseRequestListener requestListner){
		final BaseRequestListener listener = requestListner;
		if(!mFacebook.isSessionValid()){
			mbList = true;
			mRequestListner = requestListner; 
			login(null);
		}else{
			getRequestFaceBook(new BaseRequestListener() {

				@Override
				public void onComplete(String response, Object state) {
					try {
						JSONObject json = Util.parseJson(response);   
						USER_ID = json.getString("id");

					} catch (JSONException e) {
						Log.w("Facebook-Example", "JSON Error in response");
					} catch (FacebookError e) {
						Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
					}
					if(listener != null){
						mAsyncRunner.request("me/friends", listener);
					}else{
						mAsyncRunner.request("me/friends", new FriendsRequestListener());

					}
				}

				@Override
				public void onFacebookError(FacebookError e, Object state) {
					if(listener != null){
						listener.onFacebookError(new FacebookError("login fail"), null);
					}
					super.onFacebookError(e, state);
				}

				@Override
				public void onFileNotFoundException(FileNotFoundException e,
						Object state) {
					if(listener != null){
						listener.onFacebookError(new FacebookError("login fail"), null);
					}
					super.onFileNotFoundException(e, state);
				}

				@Override
				public void onIOException(IOException e, Object state) {
					if(listener != null){
						listener.onFacebookError(new FacebookError("login fail"), null);
					}
					super.onIOException(e, state);
				}

				@Override
				public void onMalformedURLException(MalformedURLException e,
						Object state) {
					if(listener != null){
						listener.onFacebookError(new FacebookError("login fail"), null);
					}
					super.onMalformedURLException(e, state);
				}

			});
		}
	}

	public class FriendsRequestListener extends BaseRequestListener {

		public void onComplete(final String response, final Object state) {
			try {
				// process the response here: executed in background thread
				final JSONObject json = new JSONObject(response);
				JSONArray d = json.getJSONArray("data");
				int l = (d != null ? d.length() : 0);
				Log.d("Facebook-Example-Friends Request", "d.length(): " + l);

				for (int i=0; i<l; i++) {
					JSONObject o = d.getJSONObject(i);
					String n = o.getString("name");
					String id = o.getString("id");
					Log.w("leeokm","friend : " + n + ", " + id);

				}

			} catch (JSONException e) {
				Log.w("Facebook-Example", "JSON Error in response");
			}
		}
	}

	//*******************************************************************************************************

	//*******************************************************************************************************
	/*
	 * 요청
	 */

	public void requestSend(String sTo, String sMsg, DialogListener dialogListener){
		Bundle params = new Bundle();
		params.putString("message", sMsg);
		params.putString("to", sTo);
		mFacebook.dialog(mActivity, "apprequests", params, dialogListener);

	}

	//*******************************************************************************************************


	//*******************************************************************************************************
	/*
	 * Feed
	 */

	public void feedSend(String sTo, String sName, String sCaption, String sMessage, String sPicture, BaseRequestListener requestListner){
		final BaseRequestListener listener = requestListner;
		mTo = sTo;
		mName = sName;
		mCaption = sCaption;
		mMessage = sMessage;
		mPicture = sPicture;
		if(!mFacebook.isSessionValid()){
			mbFeed = true;
			mRequestListner = requestListner; 
			login(null);
		}else{
			getRequestFaceBook(new BaseRequestListener() {

				@Override
				public void onComplete(String response, Object state) {
					try {
						JSONObject json = Util.parseJson(response);   
						USER_ID = json.getString("id");

					} catch (JSONException e) {
						Log.w("Facebook-Example", "JSON Error in response");
					} catch (FacebookError e) {
						Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
					}
					if(listener != null){
						Bundle parameters = new Bundle();
						parameters.putString("name", "[마크로밀엠브레인]");
						parameters.putString("caption", mCaption);
						parameters.putString("message", mMessage);
						parameters.putString("picture", mPicture);
						if(mTo.equals("")){
							mAsyncRunner.request("me/feed", parameters, "POST", listener, null);
						}else{
							mAsyncRunner.request(mTo + "/feed", parameters, "POST", listener, null);
						}
					}else{
						listener.onFacebookError(new FacebookError("feed fail"), null);
					}
				}

				@Override
				public void onFacebookError(FacebookError e, Object state) {
					if(listener != null){
						listener.onFacebookError(new FacebookError("login fail"), null);
					}
					super.onFacebookError(e, state);
				}

				@Override
				public void onFileNotFoundException(FileNotFoundException e,
						Object state) {
					if(listener != null){
						listener.onFacebookError(new FacebookError("login fail"), null);
					}
					super.onFileNotFoundException(e, state);
				}

				@Override
				public void onIOException(IOException e, Object state) {
					if(listener != null){
						listener.onFacebookError(new FacebookError("login fail"), null);
					}
					super.onIOException(e, state);
				}

				@Override
				public void onMalformedURLException(MalformedURLException e,
						Object state) {
					if(listener != null){
						listener.onFacebookError(new FacebookError("login fail"), null);
					}
					super.onMalformedURLException(e, state);
				}

			});
		}
	}

	//*******************************************************************************************************

}

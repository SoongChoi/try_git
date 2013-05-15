package com.kt.glos;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kt.glos.MainLists.MainListSLide;
import com.kt.glos.alert.Dialog_Basic_EditText_2Input;
import com.kt.glos.data.FaceBookItem;
import com.kt.glos.data.UserItem;
import com.kt.glos.dialog.InterDialogResult;
import com.kt.glos.facebook.DialogError;
import com.kt.glos.facebook.Facebook.DialogListener;
import com.kt.glos.facebook.FacebookError;
import com.kt.glos.network.JASONNetWork;
import com.kt.glos.network.NetWorkComplete;
import com.kt.glos.network.UrlList;
import com.kt.glos.twitter.TwitterLogin;
import com.kt.glos.util.LogTag;

public class Intro extends BaseActivity {

	String userID;
	String userPWD;
	String loginType;
	final String FB_LOGIN = "facebook";
	final String TW_LOGIN = "twitter";
	final String EMAIL_LOGIN = "email";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setMyContentView(R.layout.intro);
		((Button)findViewById(R.id.btn01)).setOnClickListener(this);
		((Button)findViewById(R.id.btn02)).setOnClickListener(this);
		((Button)findViewById(R.id.btn03)).setOnClickListener(this);
	}
	public boolean onBaseActivityClick(View v)
	{
		switch(v.getId())
		{
		case R.id.btn01:
			logInFaceBook();
			break;
		case R.id.btn02:
			logInTwitter();
			break;
		case R.id.btn03:
			loginDialog();
			break;
		}
		return true;
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		System.exit(0);
		super.onBackPressed();
	}
	public void logInFaceBook()
	{
		if(mApplication.getFaceBook().isSessionValid())
		{
			getFacebookBassOAuth(mApplication.getFaceBook().mFacebook.getAccessToken());
		}
		else
		{
			mApplication.getFaceBook().login(new DialogListener() {

				@Override
				public void onFacebookError(FacebookError e) {
					// TODO Auto-generated method stub
					LogTag.e("Facebook", "onFacebookError");
				}

				@Override
				public void onError(DialogError e) {
					// TODO Auto-generated method stub
					LogTag.e("Facebook", "onError");
				}

				@Override
				public void onComplete(Bundle values) {
					// TODO Auto-generated method stub
					getFacebookBassOAuth(mApplication.getFaceBook().mFacebook.getAccessToken());
				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
					LogTag.e("Facebook", "onCancel");
				}
			});
		}
	}
	public void logInTwitter()
	{
		Intent mIntent = new Intent(getBaseContext(),TwitterLogin.class);
		startActivityForResult(mIntent, TwitterLogIn);
	}

	public void getFacebookBassOAuth(String oauth) {
		ArrayList<String>nameList = new ArrayList<String>();
		ArrayList<String>postList = new ArrayList<String>();

		String urlString = UrlList.FacebookOAUTH;
		String dataType = UrlList.GET;

		nameList.add("fb_access_token");
		postList.add(oauth);

		loginType = FB_LOGIN;

		JASONNetWork mNet = new JASONNetWork(this, nameList, postList, urlString, dataType);
		mNet.setNetWorkComplete(mNetworkComplete);
		mNet.setProgress(true);
		mNet.onTimeConnect(3,10000);
	}



	public void loginDialog() {
		new Dialog_Basic_EditText_2Input(this, new InterDialogResult() {

			@Override
			public void resultdialogObject(Object obj, String returnType) {
				// TODO Auto-generated method stub
				String[] result = (String[])obj;
				if("ok".equals(result[0]))
				{
					if(!"".equals(result[1])&&!"".equals(result[2]))
					{
						getEmailLogin(result[1], result[2]);
					}
					else
					{
						Toast.makeText(getBaseContext(), "아이디/비밀번호를 확인해 주시기 바랍니다.", 1000).show();
					}
				}
			}
		}).show("로그인", "아이디/패스워드를\n입력해 주세요.", "이메일", "비밀번호");
	}

	public void getEmailLogin(String id, String pwd) {
		ArrayList<String>nameList = new ArrayList<String>();
		ArrayList<String>postList = new ArrayList<String>();

		String urlString = UrlList.OAUTH_URL+"token";
		String dataType = UrlList.GET;

		nameList.add("grant_type");
		postList.add("password");

		nameList.add("username");	
		postList.add(id);

		nameList.add("password");
		postList.add(pwd);

		loginType = EMAIL_LOGIN;
		JASONNetWork mNet = new JASONNetWork(this, nameList, postList, urlString, dataType);
		mNet.setNetWorkComplete(mNetworkComplete);
		mNet.setProgress(true);
		mNet.onTimeConnect(3,20000);
	}

	public void parseLogin(String jsonData) {
		UserItem userItem = new UserItem(jsonData);
		mApplication.setUserItem(userItem);

		if(loginType.equals(FB_LOGIN))
		{
			FaceBookItem fbItem = new FaceBookItem(userItem.getFacebook());
			mApplication.setFacebookItem(fbItem);
		}

		Intent mIntent = new Intent(getBaseContext(),MainListSLide.class);
		startActivity(mIntent);

	}

	NetWorkComplete mNetworkComplete = new NetWorkComplete() {

		@Override
		public void onNetWorkComplete(boolean isSuccess, String jsonData) {
			// TODO Auto-generated method stub
			if(isSuccess)
			{
				if(loginType.equals(EMAIL_LOGIN))
				{
					parseLogin(jsonData);
				}
				else if(loginType.equals(FB_LOGIN))
				{
					//					TestFaceBookUer mTestFaceBookUer = new TestFaceBookUer(Intro.this);
					//					mTestFaceBookUer.show(jsonData);
					parseLogin(jsonData);
				}
			}
			else
			{
				Toast.makeText(getBaseContext(), UrlList.Error, 1000).show();
			}
		}
	};
}

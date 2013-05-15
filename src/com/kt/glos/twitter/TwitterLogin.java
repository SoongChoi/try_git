package com.kt.glos.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kt.glos.R;
import com.kt.glos.WebActivity;
import com.kt.glos.twitter.RequestTockenAsysncTask.TockenComplete;

public class TwitterLogin extends WebActivity
{
	Intent mIntent;
	Twitter mTwitter;
	RequestToken mRqToken;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setMyContentView(R.layout.twitter_login);
		setWebView();
		getLogInUrl();
	}
	public void webPageStarted(String url)
	{
		if (url != null && url.startsWith(TwitterConstant.TWITTER_CALLBACK_URL)) 
		{
			mWebview.setVisibility(View.INVISIBLE);
			String[] urlParameters = url.split("\\?")[1].split("&");

			String oauthToken = "";
			String oauthVerifier = "";

			try {
				if (urlParameters[0].startsWith("oauth_token")) {
					oauthToken = urlParameters[0].split("=")[1];
				} else if (urlParameters[1].startsWith("oauth_token")) {
					oauthToken = urlParameters[1].split("=")[1];
				}

				if (urlParameters[0].startsWith("oauth_verifier")) {
					oauthVerifier = urlParameters[0].split("=")[1];
				} else if (urlParameters[1].startsWith("oauth_verifier")) {
					oauthVerifier = urlParameters[1].split("=")[1];
				}

				RequestTockenAsysncTask mRequestTockenAsysncTask =	new RequestTockenAsysncTask(this,mTwitter);

				mRequestTockenAsysncTask.setTockenComplete(new TockenComplete() {

					@Override
					public void onTockenComplete(boolean isSuccess, Object mObject) {
						// TODO Auto-generated method stub
						if(isSuccess){
							TUtil tUtil = new TUtil();
							tUtil.setAppPreferences(TwitterLogin.this, ((AccessToken)mObject).getToken(), ((AccessToken)mObject).getTokenSecret());
							setResult(RESULT_OK, mIntent);
							finish();
						}
						else{
							finish();
						}
					}
				});
				mRequestTockenAsysncTask.onOAuthConnect(mRqToken, oauthVerifier);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	} 
	public void getLogInUrl()
	{
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey(TwitterConstant.GLOS_TWITTER_CONSUMER_KEY);
		cb.setOAuthConsumerSecret(TwitterConstant.GLOS_TWITTER_CONSUMER_SECRET);
		TwitterFactory factory = new TwitterFactory(cb.build());
		mTwitter = factory.getInstance();

		RequestTockenAsysncTask mRequestTockenAsysncTask =	new RequestTockenAsysncTask(this,mTwitter);

		mRequestTockenAsysncTask.setTockenComplete(new TockenComplete() {

			@Override
			public void onTockenComplete(boolean isSuccess, Object mObject) {
				// TODO Auto-generated method stub
				if(isSuccess){
					TwitterLogin.this.mRqToken = (RequestToken)mObject;
					mWebview.loadUrl(mRqToken.getAuthorizationURL());
				}
				else{
					finish();
				}
			}
		});
		mRequestTockenAsysncTask.onCallBackConnect();
	}
}



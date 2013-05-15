package com.kt.glos.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;

public class TwitterWrite {

	Activity mActivity;
	public TwitterWrite(Activity activity)
	{
		mActivity = activity;
	}

	public void write(String data)
	{
		TUtil tUtil = new TUtil();
		String accessToken = tUtil.getToken(mActivity);
		String accessTokenSecret = tUtil.getSecretTokenKey(mActivity);

		AccessToken mAccessToken = new AccessToken(accessToken, accessTokenSecret);
		try {

			ConfigurationBuilder cb = new ConfigurationBuilder();
			String oAuthAccessToken = mAccessToken.getToken();
			String oAuthAccessTokenSecret = mAccessToken.getTokenSecret();
			String oAuthConsumerKey = TwitterConstant.GLOS_TWITTER_CONSUMER_KEY;
			String oAuthConsumerSecret = TwitterConstant.GLOS_TWITTER_CONSUMER_SECRET;
			cb.setOAuthAccessToken(oAuthAccessToken);
			cb.setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
			cb.setOAuthConsumerKey(oAuthConsumerKey);
			cb.setOAuthConsumerSecret(oAuthConsumerSecret);
			Configuration config = cb.build();

			TwitterFactory tFactory = new TwitterFactory(config);
			Twitter twitter = tFactory.getInstance();


			twitter.updateStatus(data);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

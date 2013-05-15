package com.kt.glos;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.kt.glos.util.LogTag;

public class WebActivity extends BaseActivity{
	public ProgressBar progressBar;
	public WebView mWebview;
	public ArrayList <String> urlList;
	public CookieManager cookieManager;
	public boolean history = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		CookieSyncManager.createInstance(this);
		cookieManager = CookieManager.getInstance();
		super.onCreate(savedInstanceState);
	}
	public void setWebView(){
		urlList = new ArrayList <String>();
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		mWebview = (WebView)findViewById(R.id.webview);


		mWebview.clearCache(true);
		mWebview.setWebViewClient(new MyWebClient());
		mWebview.setWebChromeClient(new MyWebChromeClient());
		WebSettings set = mWebview.getSettings();
		set.setJavaScriptEnabled(true);
		set.setSupportZoom(false);
		set.setBuiltInZoomControls(false);
		set.setLoadWithOverviewMode(true);
		set.setUseWideViewPort(true);
//		set.setDefaultZoom(ZoomDensity.MEDIUM);
//		mWebview.setInitialScale(1);
	}
	public class MyWebClient extends WebViewClient{
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url){
			LogTag.d("kim","shouldOverrideUrlLoading="+url);
			if(!isAppUrl(url)) view.loadUrl(url);
			return true;
		}
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			LogTag.d("kim","onPageStarted="+url);
			webPageStarted(url);
		}
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			LogTag.d("kim","onPageFinished="+url);
			if(history && !urlList.contains(url))urlList.add(url);
			super.onPageFinished(view, url);
			webPageFinished(url);
		}
	}
	class MyWebChromeClient extends  WebChromeClient
	{
		public void onProgressChanged(WebView view, int progress) 
		{
			// Activities and WebViews measure progress with different scales.     
			// The progress meter will automatically disappear when we reach 100%
			if(progress<=99){
				progressBar.setVisibility(View.VISIBLE);
			}
			else{
				progressBar.setVisibility(View.INVISIBLE);  
			}
		}
	}

	@Override
	public void onResume(){
		super.onResume();
		CookieSyncManager.getInstance().startSync();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	@Override
	public void onDestroy(){
		CookieSyncManager.getInstance().stopSync();

		if(cookieManager != null)cookieManager.removeAllCookie();
		mWebview.destroy(); 
		mWebview=null;
		super.onDestroy();
	}
	
	
	
	public boolean isAppUrl(String url)
	{
		return false;
	}
	public void webPageStarted(String url)
	{
		
	}
	public void webPageFinished(String url)
	{
		
	}
}

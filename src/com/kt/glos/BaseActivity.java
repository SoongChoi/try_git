package com.kt.glos;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.kt.glos.alert.Dialog_Basic_OneButton;
import com.kt.glos.facebook.SessionStore;
import com.kt.glos.network.MyProgressDialog;
import com.kt.glos.util.LogTag;

public class BaseActivity extends Activity implements OnClickListener{

	public GlosApplication mApplication = null;
	private boolean clickable;
	MyProgressDialog mProgressDialog;
	public int pxWidth=0; 
	public int pxHeight=0;
	final int TwitterLogIn = 0x5285534;
	final int FacabookLogIn = 0x32665;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mApplication = (GlosApplication)getApplicationContext();
		mApplication.getFaceBook().setActivity(this);
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		mApplication.getFaceBook().setActivity(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		clickable = true;
		super.onResume();
	}

	public void setMyContentView(int layoutId)
	{
		if(getLocalClassName().equals("IntroActivity") 
				||getLocalClassName().equals("twitter.TwitterLogin")){
			setContentView(layoutId);
		}
		else{
			setContentView(setScreenContentView(layoutId));
		}
	}

	public int setScreenContentView(int layoutId)
	{
		String layoutName = getResources().getResourceEntryName(layoutId);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		pxWidth  = displayMetrics.widthPixels;
		pxHeight = displayMetrics.heightPixels;
		float densitys = displayMetrics.density*160;
		/*LogTag.d("kim","pxWidth =" +pxWidth);
		LogTag.d("kim","pxHeight =" +pxHeight);
		LogTag.d("kim","densitys =" +densitys);*/

		/**갤럭시 S 개열**/
		if(pxWidth<=480 && densitys==160){
			layoutName = layoutName+"_10_160";
		}
		/**갤럭시 S 개열**/
		else if(pxWidth<=480 && densitys==240){
			layoutName = layoutName+"_10_240";
		}
		/**갤럭시 S2 보다 큰 개열**/
		else if(480<pxWidth && pxWidth<800){
			layoutName = layoutName+"_15_320";
		}
		/**갤럭시 10.01**/
		else if(pxWidth>=800 && densitys<=240){
			layoutName = layoutName+"_16_160";
		}
		/**갤럭시 노트 개열**/
		else if(pxWidth>=800 && densitys>240){
			layoutName = layoutName+"_16_320";
		}
		int value = getResources().getIdentifier(layoutName, "layout", getPackageName());
		return value;
	}

	@Override
	public void onClick(View v) {
		v.setClickable(false);
		if (clickable) {
			clickable = false;
			if(onBaseActivityClick(v)){
				clickReset();
			}
		}
		v.setClickable(true);
	}

	public void clickReset() {
		clickable = true;
	}

	public boolean onBaseActivityClick(View v) {
		return true;
	}


	public boolean isNetWorkState(){

		ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(!mobile.isConnected()&&!wifi.isConnected())
		{
			new Dialog_Basic_OneButton(this, null).show("인터넷 접속 오류", 
					"3G/wifi 접속 후 사용이 가능합니다.", false);
			return false;
		}
		else
		{
			return true;
		}
	}







	//==================ImageSynkTask=============================
	ArrayList<ImageTask> asynkList;

	public void getUrlImage(ImageView mImageView,String urlString)
	{
		//		if( mImageView.getTag(ImageKey) != null) return;
		ImageTask mIImageTask = new ImageTask(mImageView,urlString);
		mIImageTask.execute();
	}
	public void setImageTaskRemove()
	{
		if(asynkList == null){
			return;
		}
		for(int i=0;i<asynkList.size();i++)
		{
			((ImageTask)asynkList.get(i)).isCancel = true;
			((ImageTask)asynkList.get(i)).cancel(true);
		}
	}

	class ImageTask extends AsyncTask<String, Void, Bitmap>
	{
		ImageView  mImageView;
		String urlString;
		public boolean isCancel = false;

		public ImageTask(ImageView mImageView,String urlString) {
			// TODO Auto-generated constructor stub
			this.urlString = urlString;
			this.mImageView = mImageView;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if(asynkList == null){
				asynkList = new ArrayList<ImageTask>();
			}
			asynkList.add(BaseActivity.ImageTask.this);
			super.onPreExecute();
		}
		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			if(!isCancel)	return setUrlImg2(urlString);
			else return null;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			if(isCancel)
			{
				super.onPostExecute(null);
			}
			else
			{
				if(result != null && mImageView != null){
					mImageView.setImageBitmap(result);
					mImageView.setTag("BitmapExist");
				}
				asynkList.remove(BaseActivity.ImageTask.this);
				super.onPostExecute(result);
			}
		}
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

	}
	/**개별 사진 다운로드*/
	public Bitmap setUrlImg2(String urlString)
	{
		Bitmap bitmap= null;
		HttpURLConnection conn = null;
		BufferedInputStream bis = null;
		File mFile = null; 
		try
		{
			URL url = new URL(urlString); 
			conn =(HttpURLConnection) url.openConnection();
			if(conn.getResponseCode()== HttpURLConnection.HTTP_OK)
			{
				mFile =  File.createTempFile("Images", ".jpg");
				FileOutputStream out = new FileOutputStream(mFile);
				byte buffer[] = new byte[5120];
				bis = new BufferedInputStream(conn.getInputStream()); 
				int nRead;
				while((nRead = bis.read(buffer, 0, buffer.length)) != -1)
				{
					out.write(buffer, 0, nRead);
				}

				out.close();
				bis.close();
				bitmap = BitmapFactory.decodeFile(mFile.getPath());
				conn.disconnect();
			}
			else 
			{
				conn.disconnect();
			}

		} 
		catch (IOException ex) 
		{
			if(ex.getMessage()!=null)
			{
				LogTag.i("kim", ex.getMessage());
				if(mFile != null && mFile.exists()){
					mFile.delete();
					mFile = null;
				}
				if (conn!= null) {
					conn.disconnect();
				}
			}
		}
		if(mFile != null && mFile.exists())mFile.delete();
		return bitmap;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == FacabookLogIn && resultCode == RESULT_OK){
			mApplication.getFaceBook().mFacebook.setAccessToken(data.getStringExtra("access_token"));
			mApplication.getFaceBook().mFacebook.setAccessExpiresIn(data.getStringExtra("expires_in"));
			SessionStore.save(mApplication.getFaceBook().mFacebook,this);
			mApplication.getFaceBook().mFacebook.authorizeCallback(requestCode, resultCode, data);
		}
		else if(requestCode == TwitterLogIn){
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}

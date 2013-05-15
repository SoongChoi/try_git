package com.kt.glos.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BitmapImageView extends ImageView {

	String imagePath="";
	boolean isCache;
	boolean isCancle;
	Bitmap bitmap= null;
	boolean isDowning;
	ColorDrawable mColorDrawable;
	BitmapThread mBitmapThread;
	BitmapHandler mBitmapHandler;
	int position=-1;
	public BitmapImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mColorDrawable = new ColorDrawable(Color.BLACK);
	}
	public BitmapImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mColorDrawable = new ColorDrawable(Color.BLACK);
	}
	public BitmapImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mColorDrawable = new ColorDrawable(Color.BLACK);
	}
	public class BitmapHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==0){
				setImageBitmap(bitmap);
				mBitmapThread = null;
			}
			else if(msg.what==-1){
				mBitmapThread = null;
			}
		}
	}
	public void setNetImage(int position,String imagePath,boolean isCache)
	{
		if(this.position == position ) {
			LogTag.d("kim","return");
			return; 
		}
		this.position = position;
		this.imagePath = imagePath;
		this.isCache = isCache;
		isCancle = false;
		mBitmapHandler = new BitmapHandler();
		
		mBitmapThread = new BitmapThread();
		mBitmapThread.start();
	}
	public void setDownCancle()
	{
		setImageDrawable(mColorDrawable);
		if(mBitmapThread != null && mBitmapThread.isAlive()) {
			isCancle = true;
		}
		else{
			mBitmapHandler.sendEmptyMessage(-1);
		}
	}
	public class BitmapThread extends Thread
	{
		public void run() 
		{
			HttpURLConnection conn = null;
			BufferedInputStream bis = null;
			File mFile = null; 
			isDowning = true;
			try
			{
				URL url = new URL(imagePath); 
				conn =(HttpURLConnection) url.openConnection();
				if(conn.getResponseCode()== HttpURLConnection.HTTP_OK)
				{
					mFile =  File.createTempFile("Images", ".jpg");
					FileOutputStream out = new FileOutputStream(mFile);
					byte buffer[] = new byte[5120];
					bis = new BufferedInputStream(conn.getInputStream()); 
					int nRead;
					while((nRead = bis.read(buffer, 0, buffer.length)) != -1 && ! isCancle)
					{
						out.write(buffer, 0, nRead);
					}
					out.close();
					bis.close();
					if(! isCancle) {
						bitmap = BitmapFactory.decodeFile(mFile.getPath());
						mBitmapHandler.sendEmptyMessage(0);
					}
					else{
						mBitmapHandler.sendEmptyMessage(-1);
					}
				}
				else 
				{
					conn.disconnect();
					conn = null;
					mBitmapHandler.sendEmptyMessage(-1);
				}

			} 
			catch (IOException ex) 
			{
				if(ex.getMessage()!=null)
				{
					LogTag.i("kim", ex.getMessage());
					mBitmapHandler.sendEmptyMessage(-1);
				}
			}
			if(mFile != null && mFile.exists()){
				mFile.delete();
				mFile = null;
			}
			if (conn!= null) {
				conn.disconnect();
				conn = null;
			}
			isDowning =false;
			mBitmapThread = null;
		}
	}
	public String getSdPath()
	{
		String exist= Environment.getExternalStorageState();
		String mSdPath;
		if(exist.equals(Environment.MEDIA_MOUNTED))
		{
			mSdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		else 
		{
			mSdPath = Environment.MEDIA_UNMOUNTED;
		}
		File home = new File(mSdPath,"/GLOS");
		if(!home.exists()){
			home.mkdir();
		}
		File temp = new File(home,"/Thumb");
		if(!temp.exists()){
			temp.mkdir();
		}
		return temp.getPath();
	}

}

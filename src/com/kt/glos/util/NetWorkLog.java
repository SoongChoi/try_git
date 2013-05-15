package com.kt.glos.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;

public class NetWorkLog {
	public void writeNetWorkLog(String startTime, String url, String message,int responseCode,String responseBody)
	{
		File mFile = new File(getSdPath(),"통신로그.text");
		try 
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(mFile,true));

			out.write("=================================\n");
			out.write("요청시간 :"+startTime+"\n");
			out.write("요청 URL :"+url+"\n");
			out.write(message+"\n");
			out.write("응답 :"+responseBody+"\n");
			out.write("응답시간 :"+getSystemDateNTime()+"\n");
			out.write("=================================\n");
			out.flush();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		File temp = new File(home,"/통신 로그");
		if(!temp.exists()){
			temp.mkdir();
		}
		return temp.getPath();
	}
	public String getSystemDateNTime()
	{
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "["+formatter.format(date)+"]";
	}
}

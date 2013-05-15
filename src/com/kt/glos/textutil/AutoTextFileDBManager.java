package com.kt.glos.textutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;

import com.kt.glos.util.LogTag;

public class AutoTextFileDBManager {
	Context context;
	String fileName;
	public AutoTextFileDBManager(Context context,String fileName) {
		this.context = context;
		this.fileName = fileName;
	}
	public void saveFile(ArrayList<AutoTextDBadapter>mList)
	{
		if(mList.isEmpty())
		{
			return;
		}

		ObjectOutputStream os = null;
		FileOutputStream fs = null;

		File mFile = null;
		try{
			mFile = new File(getSdPath(),"AutoTExtDB_"+fileName+".ser");
			fs = new FileOutputStream(mFile);
			os = new ObjectOutputStream(fs);
			os.writeObject(mList);

		}
		catch (Exception ex)
		{
			LogTag.e("autotext",  ex.getMessage());

		}finally{
			try {
				fs.close();
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LogTag.e("autotext",  e.getMessage());
			}	
		}

	}
	public ArrayList<AutoTextDBadapter> openFile()
	{
		ArrayList<AutoTextDBadapter> mList = new ArrayList<AutoTextDBadapter>();
		File mFile  = new File(getSdPath(),"AutoTExtDB_"+fileName+".ser");
		ObjectInputStream is = null;
		try
		{
			is = new ObjectInputStream(new FileInputStream(mFile));
			mList = (ArrayList<AutoTextDBadapter>)is.readObject(); 
		}
		catch (Exception ex)
		{
			LogTag.e("autotext",  ex.getMessage());
			return null;
		}finally{
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LogTag.e("autotext",  e.getMessage());
			}
		}
		return mList;
	}

	public boolean fileExist()
	{
		try{
			File mFile = new File(getSdPath(),"AutoTExtDB_"+fileName+".ser");
			if(mFile.exists())
			{
				return true;
			}
			else{
				return false;
			}
		}
		catch(Exception ex){
			LogTag.e("autotext",  ex.getMessage());
			return false;
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
		File temp = new File(home,"/TextData");
		if(!temp.exists()){
			temp.mkdir();
		}
		return temp.getPath();
	}

}

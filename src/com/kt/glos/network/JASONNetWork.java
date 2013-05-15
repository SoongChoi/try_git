package com.kt.glos.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.kt.glos.BaseActivity;
import com.kt.glos.alert.Dialog_Basic_OneButton;
import com.kt.glos.util.LogTag;
import com.kt.glos.util.NetWorkLog;

public class JASONNetWork {
	MyProgressDialog mProgressDialog;
	String paramValue;
	ArrayList<String> nameList = new ArrayList<String>();
	ArrayList<String> postList = new ArrayList<String>();
	String urlString;
	String dataType;
	StringBuffer json;
	Handler mNetWorkHandler;
	NetWorkComplete mNetWorkComplete;
	Activity mActivity;
	boolean isShow=false;

	NetWorkTimeCheck  mNetWorkTimeCheck;

	int repeatNum;
	int excuteNum;
	long timeOut;

	int responseCode;
	String requestTime;
	boolean isOauth;

	public void setNetWorkComplete(NetWorkComplete mNetWorkComplete){
		this.mNetWorkComplete = mNetWorkComplete;
	}

	//TODO Jasonnetwork
	public JASONNetWork(Activity activity, ArrayList<String> nameList, ArrayList<String> postList, String urlString, String dataType){
		this.mActivity = activity;
		this.nameList = nameList;
		this.postList = postList;
		this.urlString = urlString;
		this.dataType = dataType;
		LogTag.d("glos","JasonNetWork create======");
		mNetWorkHandler = new NetWorkHandler();
	}
	public class NetWorkHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

//			NetWorkThread mNetWorkThread =(NetWorkThread) msg.obj;
//			mNetWorkThread.inResponse = true;
			if(mProgressDialog != null)
			{
				mProgressDialog.dismiss();
			}
			if(msg.what==0)
			{
				if(mNetWorkComplete != null)
				{
					mNetWorkComplete.onNetWorkComplete(true, json.toString());
				}
			}
			if(msg.what==-1)
			{
				LogTag.d("glos","JasonNetWork error =-1");
				if(json ==null) json = new StringBuffer();
				writeNetWorkLog(json.toString());
				if(excuteNum<repeatNum)
				{
					mNetWorkHandler.removeCallbacks(mNetWorkTimeCheck);
					mNetWorkTimeCheck = null;

					onReconnect();
				}
				else
				{
					if(mNetWorkComplete != null)
					{
						mNetWorkComplete.onNetWorkComplete(false, json.toString());
					}
				}
			}
		}

	};
	public void setProgress(boolean isShow)
	{
		this.isShow = isShow;
	}
	public void onReconnect()
	{

		if(!((BaseActivity)mActivity).mApplication.networkEnable){
			new Dialog_Basic_OneButton(mActivity, null).show("인터넷 접속 오류", 
					"3G/wifi 접속 후 사용이 가능합니다.", false);
			return;
		}

		if(!isShow){
			mProgressDialog = null;
		}
		else{
			mProgressDialog = new MyProgressDialog(mActivity);
			mProgressDialog.show("재실행중입니다.["+excuteNum+"]\n잠시만 기다려 주세요.");
		}

		NetWorkThread mNetWorkThread = new NetWorkThread();
		mNetWorkThread.start();
		
		mNetWorkTimeCheck = new NetWorkTimeCheck(mNetWorkThread);
		excuteNum = excuteNum+1;
		mNetWorkHandler.postDelayed(mNetWorkTimeCheck, timeOut);
	}
	public void onTimeConnect(int repeatNum,long timeOut)
	{
		this.repeatNum = repeatNum;
		this.timeOut = timeOut;

		if(!((BaseActivity)mActivity).mApplication.networkEnable){
			new Dialog_Basic_OneButton(mActivity, null).show("인터넷 접속 오류", 
					"3G/wifi 접속 후 사용이 가능합니다.", false);
			return;
		}

		if(!isShow){
			mProgressDialog = null;
		}
		else{
			mProgressDialog = new MyProgressDialog(mActivity);
			mProgressDialog.show("잠시만 기다려 주세요.");
		}

		NetWorkThread mNetWorkThread = new NetWorkThread();
		mNetWorkThread.start();

		mNetWorkTimeCheck = new NetWorkTimeCheck(mNetWorkThread);
		excuteNum = excuteNum+1;
		mNetWorkHandler.postDelayed(mNetWorkTimeCheck, timeOut);
	}
	public class NetWorkThread extends Thread
	{
		boolean isConnection = false;
		boolean inResponse=false;
		HttpURLConnection connection;
		public void run()
		{
			try 
			{
				requestTime = getSystemDateNTime(); 
				isConnection =true;
				if(dataType.equals(UrlList.GET))//TODO
				{
					for(int i=0;i<nameList.size();i++)
					{
						if(i==0){
							paramValue = nameList.get(i)+"="+URLEncoder.encode(postList.get(i), "UTF-8");
						}
						else{
							paramValue = paramValue+"&"+nameList.get(i)+"="+URLEncoder.encode(postList.get(i), "UTF-8");
						}
					}

					URL url = new URL(urlString+"?"+paramValue);
					connection = (HttpURLConnection) url.openConnection();
					connection.setDoInput(true);
					connection.setUseCaches(false);
					connection.setRequestMethod(dataType);
					connection.setRequestProperty("Accept-Charset", HTTP.UTF_8);
					if( ((BaseActivity)mActivity).mApplication.getUserItem() != null
							&& ((BaseActivity)mActivity).mApplication.getUserItem().getAccess_token() != null)
						connection.setRequestProperty("Authorization"," Bearer "+((BaseActivity)mActivity).mApplication.getUserItem().getAccess_token());
					connection.setConnectTimeout((int)timeOut);
					connection.setReadTimeout((int)timeOut);
				}
				else if(dataType.equals(UrlList.POST))//TODO
				{
					URL url = new URL(urlString);
					connection = (HttpURLConnection) url.openConnection();
					connection.setDoInput(true);
					connection.setDoOutput(true);
					connection.setUseCaches(false);
					connection.setRequestProperty("Accept-Charset", HTTP.UTF_8);
					if( ((BaseActivity)mActivity).mApplication.getUserItem() != null
							&& ((BaseActivity)mActivity).mApplication.getUserItem().getAccess_token() != null)
						connection.setRequestProperty("Authorization"," Bearer "+((BaseActivity)mActivity).mApplication.getUserItem().getAccess_token());
					connection.setRequestMethod(dataType);
					connection.setConnectTimeout((int)timeOut);
					connection.setReadTimeout((int)timeOut);
					
					
					if(nameList.isEmpty())
					{
						DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
						dos.writeBytes("");
						dos.flush();
						dos.close();
					}
					else 
					{
						DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
						for(int i=0;i<nameList.size();i++)
						{
							if(i==0){
								paramValue = nameList.get(i)+URLEncoder.encode(postList.get(i), "UTF-8");
							}
							else{
								paramValue = paramValue+"&"+nameList.get(i)+URLEncoder.encode(postList.get(i), "UTF-8");
							}
						}
						dos.writeBytes(paramValue);
						dos.flush();
						dos.close();
					}
				}

				responseCode = connection.getResponseCode();
				LogTag.e("glos", connection.getResponseCode()+"<=========== Receive Before");
				if(connection.getResponseCode()== HttpURLConnection.HTTP_OK)//TODO
				{
					json = new StringBuffer();
					BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
					while(isConnection)
					{
						inResponse = true;
						String line = br.readLine();
						if(line == null) break;
						json.append(replace(line)+ '\n');
					}
					br.close();
					if(isConnection) mNetWorkHandler.obtainMessage(0, this).sendToTarget();
				}
				else
				{
					json = new StringBuffer();
					BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(),"UTF-8"));
					while(isConnection)
					{
						inResponse = true;
						String line = br.readLine();
						if(line == null) break;
						json.append(replace(line)+ '\n');
					}
					br.close();
					if(isConnection) mNetWorkHandler.obtainMessage(-1, this).sendToTarget();
				}

				LogTag.e("glos", connection.getResponseCode()+"<=========== Receive after" +"");
			}
			catch (Exception ex) //TODO
			{
				LogTag.e(ex.toString());
				json = new StringBuffer();
				json.append(ex.toString());
				if(isConnection)  mNetWorkHandler.obtainMessage(-1, this).sendToTarget();
			}
			finally//TODO
			{
				connection.disconnect();
			}

		}
	}

	String replace(String someting)
	{
		return someting.replace("&amp;", "&");
	}

	public class NetWorkTimeCheck implements Runnable
	{
		NetWorkThread mNetWorkThread;
		NetWorkTimeCheck(NetWorkThread mNetWorkThread)
		{
			this.mNetWorkThread = mNetWorkThread;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(!mNetWorkThread.inResponse){
				mNetWorkThread.isConnection = false;
				if(mNetWorkThread.connection !=null )mNetWorkThread.connection.disconnect();
				mNetWorkHandler.obtainMessage(-1, mNetWorkThread).sendToTarget();
			}
		}
	}
	synchronized public void writeNetWorkLog(String responseBody)
	{
		NetWorkLog mNetWorkLog = new NetWorkLog();
		if(dataType.equals(UrlList.GET)) mNetWorkLog.writeNetWorkLog(requestTime,urlString+"?"+paramValue,"",responseCode,responseBody);
		else mNetWorkLog.writeNetWorkLog(requestTime,urlString,paramValue,responseCode,responseBody);
		if(paramValue != null)LogTag.d(paramValue);
		paramValue = null;
	}
	public String getSystemDateNTime()
	{
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "["+formatter.format(date)+"]";
	}

}

package com.kt.glos.textutil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;


public class TextAutoComplete {
	Context context;
	ArrayList<String> result = new ArrayList<String>();
	public int listCnt = 0;

	ArrayList<AutoTextDBadapter> mData = null;
	AutoTextFileDBManager mManager = null;

	public TextAutoComplete(Context context,String fileName){
		this.context = context;
		mData = new ArrayList<AutoTextDBadapter>();
		mManager = new AutoTextFileDBManager(context, fileName);
		if(mManager.fileExist())
		{
			openFile();
		}
	}

	public ArrayList<String> getResult() {

		return result;

	}

	public void openFile(){
		mData = mManager.openFile();
		listCnt = mData.size();
		if(listCnt>0)fileSort();
	}

	public void saveFile(String searchText){
		mData.clear();
		if(mManager.fileExist())
		{
			mData = mManager.openFile();
			listCnt = mData.size();

			for(int i=0;i<mData.size();i++)
			{
				if(searchText.equals(mData.get(i).getSearchText()))
				{
					mData.remove(i);
					i--;
				}
			}

			if(listCnt==50)
			{
				mData.remove(49);
			}
		}

		AutoTextDBadapter mAdapter = new AutoTextDBadapter();
		mAdapter.setData(System.currentTimeMillis()+"", searchText);

		mData.add(mAdapter);

		mManager.saveFile(mData);
	}

	public void fileSort(){
		Comparator<AutoTextDBadapter> comparItems = new Comparator<AutoTextDBadapter>() {

			@Override
			public int compare(AutoTextDBadapter lhs, AutoTextDBadapter rhs) {
				// TODO Auto-generated method stub
				int sort = 0;

				int llevel = 5;
				int rlevel = 5;

				String comparString;
				comparString = lhs.getSearchText();
				if (!"".equals(comparString)) {
					char compareFirst = comparString.charAt(0);
					if (TextSearch.isHangul(compareFirst)) {
						llevel = 1;
					} else if ('a' <= compareFirst && compareFirst <= 'Z') {
						llevel = 2;
					} else if ('0' <= compareFirst && compareFirst <= '9') {
						llevel = 3;
					} else {
						llevel = 4;

					}
				}
				comparString = rhs.getSearchText();
				if (!"".equals(comparString)) {
					char compareFirst = comparString.charAt(0);
					if (TextSearch.isHangul(compareFirst)) {
						rlevel = 1;
					} else if ('a' <= compareFirst && compareFirst <= 'Z') {
						rlevel = 2;
					} else if ('0' <= compareFirst && compareFirst <= '9') {
						rlevel = 3;
					} else {
						rlevel = 4;

					}
				}

				sort = llevel == rlevel ? lhs.getSearchText()
						.compareToIgnoreCase(rhs.getSearchText())
						: llevel > rlevel ? 1 : -1;
						return sort;
			}
		};

		Collections.sort(mData,comparItems);
		result.clear();

		for(int i=0;i<mData.size();i++)
		{
			result.add(mData.get(i).getSearchText());
		}
	}

}

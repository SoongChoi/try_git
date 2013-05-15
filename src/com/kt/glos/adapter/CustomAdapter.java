package com.kt.glos.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class CustomAdapter extends ArrayAdapter<InterAdapterView> {

	public CustomAdapter(Context context, int textViewResourceId,
			ArrayList<InterAdapterView> items) {
		super(context, textViewResourceId, items);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		return this.getItem(position).getMyView();
	}

}
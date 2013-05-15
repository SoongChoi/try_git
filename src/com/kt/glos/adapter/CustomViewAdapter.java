package com.kt.glos.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class CustomViewAdapter extends ArrayAdapter<InterAdapterGetview> {

	public CustomViewAdapter(Context context, int textViewResourceId,
			ArrayList<InterAdapterGetview> items) {
		super(context, textViewResourceId, items);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		return this.getItem(position).getMyView(position,convertView);
	}

}
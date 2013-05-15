package com.kt.glos;

import java.util.ArrayList;

import ktmap.android.map.overlay.Marker;
import ktmap.android.map.overlay.MarkersLayer;

public class MarkerLayerSample extends MarkersLayer{
	ArrayList<Marker> list = new ArrayList<Marker>();
	@Override
	protected Marker getMarker(int index) {
		// TODO Auto-generated method stub
		if(index>=list.size())return null;
		return list.get(index);
	}

	@Override
	protected int size() {
		// TODO Auto-generated method stub
		return list.size();
	}
	//리스트에 marker객체를 추가합니다.
	public void addItem(Marker mark){
		list.add(mark);
	}
	//리스트를 초기화 합니다.
	public void removeAll(){
		list = new ArrayList<Marker>();
	}
	@Override
	public void setVisible(boolean on) {
		// TODO Auto-generated method stub
		this.isVisible = on;
	}

}

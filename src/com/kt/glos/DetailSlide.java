package com.kt.glos;

import ktmap.android.map.Coord;
import ktmap.android.map.KMap;
import ktmap.android.map.overlay.Marker;
import ktmap.android.utils.Projection;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailSlide {

	Activity mActivity;
	KMap mapView;
	ImageView img;
	String mJsonData;
	String imagePath;
	float xpos=0f,ypos=0f;
	
	public DetailSlide(Activity mActivity)
	{
		this.mActivity = mActivity; 
		
		mapView = (KMap)mActivity.findViewById(R.id.mapView);
		img = (ImageView)mActivity.findViewById(R.id.img);
	}
	
	public void setData(String mJsonData,	String imagePath)
	{
		this.mJsonData = mJsonData;
		this.imagePath = imagePath;
		
		((BaseActivity)mActivity).getUrlImage(img,imagePath);
	}
	
	public void setContent(MarkerLayerSample markerlayer)
	{
		try 
		{
			JSONObject data = new JSONObject(mJsonData);
			JSONObject order = new JSONObject(data.getJSONArray("entities").get(0).toString());
			
			if(order.has("NAME")){
				TextView text01 = (TextView) mActivity.findViewById(R.id.text01);
				text01.setText(order.getString("NAME"));
			}
			if(order.has("INFO1")){
				TextView text03 = (TextView) mActivity.findViewById(R.id.text03);
				text03.setText(order.getString("INFO1"));				
			}
			if(order.has("ADDRESS")){
				TextView text04 = (TextView) mActivity.findViewById(R.id.text04);
				text04.setText(order.getString("ADDRESS"));
			}
			
			if(order.has("XPOS")){
				xpos = 	Float.valueOf(order.getString("XPOS"));		
			}
			if(order.has("YPOS")){
				ypos = 	Float.valueOf(order.getString("YPOS"));		
			}
			if(xpos != 0 && ypos != 0){
				Projection projection = mapView.getProjection();
				Coord targetCoord = new Coord(xpos,ypos);
				Coord convertedCoord = projection.transCoordination(Projection.WGS84, Projection.UTMK, targetCoord);
							
				mapView.getOverlays().add(markerlayer);
				
				
				Marker marker = new Marker(convertedCoord);
				marker.setTitle(((TextView)mActivity.findViewById(R.id.text01)).getText().toString());
				markerlayer.addItem(marker);
				
				mapView.setMapCenter(convertedCoord);
				mapView.invalidate();
			}
		} 
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void removeImage()
	{
		if(img!= null && img.getTag() != null && ((BitmapDrawable)img.getDrawable()).getBitmap() != null){
			((BitmapDrawable)img.getDrawable()).getBitmap().recycle();
			img.setImageBitmap(null);
		}
	}
}

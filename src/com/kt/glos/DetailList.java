package com.kt.glos;

import ktmap.android.map.Coord;
import ktmap.android.map.KMap;
import ktmap.android.map.overlay.Marker;
import ktmap.android.utils.Projection;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailList extends KmapBaseActivity {

	KMap mapView;
	String mJsonData;
	String imagePath;
	ImageView img;
	float xpos=0f,ypos=0f;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mJsonData = getIntent().getStringExtra("JsonData");
		imagePath = getIntent().getStringExtra("ImagePath");
		setContentView(R.layout.detail_list);
		
		mapView = (KMap)findViewById(R.id.mapView);
		mapView.setZoomLevel(10);
		mapView.dispatchMapEvent(this);
		
		
		mapView.setSatellite(false);
		mapView.setMapResolution(KMap.HD_RESOLUTION);
		//위성맵 설정
		//mapView.setSatellite(true);
		//하이브리드맵 설정
		//mapView.setHybrid(true);
		//실시간교통상황맵 설정
		//mapView.setTraffic(true);
		if(imagePath != null){
			img = (ImageView) findViewById(R.id.img);
			getUrlImage(img,imagePath);
		}
		setContent();
	}
	public void setContent()
	{
		try 
		{
			JSONObject data = new JSONObject(mJsonData);
			JSONObject order = new JSONObject(data.getJSONArray("entities").get(0).toString());
			
			if(order.has("NAME")){
				TextView text01 = (TextView) findViewById(R.id.text01);
				text01.setText(order.getString("NAME"));
			}
			if(order.has("INFO1")){
				TextView text03 = (TextView) findViewById(R.id.text03);
				text03.setText(order.getString("INFO1"));				
			}
			if(order.has("ADDRESS")){
				TextView text04 = (TextView) findViewById(R.id.text04);
				text04.setText(order.getString("ADDRESS"));
			}
			
			if(order.has("XPOS")){
				xpos = 	Float.valueOf(order.getString("XPOS"));		
			}
			if(order.has("YPOS")){
				ypos = 	Float.valueOf(order.getString("YPOS"));		
			}

			markerlayer = new MarkerLayerSample();
			
			if(xpos != 0 && ypos != 0){
				Projection projection = mapView.getProjection();
				Coord targetCoord = new Coord(xpos,ypos);
				Coord convertedCoord = projection.transCoordination(Projection.WGS84, Projection.UTMK, targetCoord);
							
				mapView.getOverlays().add(markerlayer);
				markerlayer.dispatchOverlayEvent(this);
				
				Marker marker = new Marker(convertedCoord);
				marker.setTitle(((TextView) findViewById(R.id.text01)).getText().toString());
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
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		removeImage();
		super.onDestroy();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.action_out_up, R.anim.action_out_down);
	}

}

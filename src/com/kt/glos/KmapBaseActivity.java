package com.kt.glos;

import ktmap.android.map.Bounds;
import ktmap.android.map.MapEventListener;
import ktmap.android.map.Pixel;
import ktmap.android.map.overlay.Overlay;
import ktmap.android.map.overlay.OverlayEventListener;

public class KmapBaseActivity extends BaseActivity implements OverlayEventListener,MapEventListener{

	public MarkerLayerSample markerlayer;
	
	@Override
	public boolean onBoundsChange(Bounds arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onChangeZoomLevel(boolean arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDoubleTouch(Pixel arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onLongTouch(Pixel arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMapInitializing(boolean arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMultiTouch(Pixel[] arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(Pixel arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDoubleTouch(Overlay arg0, float arg1, float arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onLongTouch(Overlay arg0, float arg1, float arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(Overlay arg0, float arg1, float arg2) {
		// TODO Auto-generated method stub
		return false;
	}

}

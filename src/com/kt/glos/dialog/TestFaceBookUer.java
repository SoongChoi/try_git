package com.kt.glos.dialog;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kt.glos.BaseActivity;
import com.kt.glos.R;

public class TestFaceBookUer extends BaseDialog{

	ImageView img;
	public TestFaceBookUer(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
	public void show(String jsonData)
	{
		super.show();
		setMyContentView(R.layout.face_profile);
		
		((Button)dialog.findViewById(R.id.closeBtn)).setOnClickListener(this);
		
		setContent(jsonData);
		dialog.show();
	}
	public void setContent(String jsonData)
	{
		try 
		{
			JSONObject order = new JSONObject(jsonData);
			
			String user = order.getString("user");
			JSONObject order2 = new JSONObject(user);
			String facebook = order2.getString("facebook");
			JSONObject order3 = new JSONObject(facebook);

			TextView text01 = (TextView)dialog.findViewById(R.id.text01);
			text01.setText(order2.getString("name"));
			
			
			
			TextView text02 = (TextView)dialog.findViewById(R.id.text02);
			text02.setText(order3.getString("link"));
			
			TextView text03 = (TextView)dialog.findViewById(R.id.text03);
			text03.setText(order3.getString("gender"));
			
			img = (ImageView)dialog.findViewById(R.id.img);
			
			((BaseActivity)activity).getUrlImage(img,order2.getString("picture"));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean onBaseDialogClick(View v) {
	
		switch(v.getId()){
		case R.id.closeBtn:
			dialog.dismiss();
			break;
		}
		((BaseActivity)activity).clickReset();
		return true;
	}
}

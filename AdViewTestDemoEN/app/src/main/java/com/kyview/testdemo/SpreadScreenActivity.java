package com.kyview.testdemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kyview.interfaces.AdViewSpreadListener;
import com.kyview.manager.AdViewSpreadManager;

/**
 *
 *Open Screen Advertisement
 *
 */
public class SpreadScreenActivity extends Activity implements
		AdViewSpreadListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.spread_layout);

		//Intialization for Open Screen ad
		AdViewSpreadManager.getInstance(this).init(MainActivity.initConfiguration, new String[]{MainActivity.SDK_KEY});


		// setting logo for open screen ad
		//User Defined Logo
		AdViewSpreadManager.getInstance(this).setSpreadLogo(R.drawable.spread_logo);

		// setting background color for open screen ad
		//User Defined Background color
		AdViewSpreadManager.getInstance(this).setSpreadBackgroudColor(
				Color.WHITE);

		//how much time you want show open screen ad
		AdViewSpreadManager.getInstance(this).setSpreadNotifyType(AdViewSpreadManager.NOTIFY_COUNTER_NUM);

		// Requesting open screen ad
		AdViewSpreadManager.getInstance(this).request(this, MainActivity.SDK_KEY,
				(RelativeLayout) findViewById(R.id.spreadlayout), this);

	}



	/*
	 * For better ad revenue, please disable the return key until the ad is displayed
	 */		
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
			return false;
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		waitingOnRestart = true;
		jumpWhenCanClick();
	}

	public boolean waitingOnRestart = false;

	private void jump() {
		// Intent intent = new Intent();
		// intent.setClass(this, MainActivity.class);
		// startActivity(intent);
		// if (null != adSpreadManager)
		// adSpreadManager.setAdSpreadInterface(null);
		this.finish();
	}

	/*
	 * This function will be called when the spread ad is clicked
	 */
	private void jumpWhenCanClick() {

		if (this.hasWindowFocus() || waitingOnRestart) {
			// this.startActivity(new Intent(this, MainActivity.class));
			// adSpreadManager.setAdSpreadInterface(null);
			this.finish();
		} else {
			waitingOnRestart = true;
		}

	}


	@Override
	public void onAdClose(String arg0) {
		jump();
	}

	@Override
	public void onAdDisplay(String arg0) {

	}

	@Override
	public void onAdFailed(String arg0) {
		jump();
	}

	@Override
	public void onAdRecieved(String arg0) {
		Toast.makeText(this, "onAdRecieved", Toast.LENGTH_SHORT).show();
	}

	/**
	 * This function will be called only when Publisher has set "adSpreadManager.setSpreadNotifyType(this,
	 * AdSpreadManager.NOTIFY_CUSTOM);"
	 *
	 * @param view
	 *            return the custom layout above (RelativeLayout)
	 * @param ruleTime
	 *
	 * @param delayTime
	 *
	 */
	@Override
	public void onAdSpreadNotifyCallback(String arg0, ViewGroup arg1, int arg2,
										 int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAdClick(String arg0) {
		// TODO Auto-generated method stub

	}
}

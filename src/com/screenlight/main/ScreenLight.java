package com.screenlight.main;

import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class ScreenLight extends Activity {
	public static final String TAG = "ScreenLight";
	private static final int MIN_LIGHT = 50;
	private static final int MAX_LIGHT = 255;
	private PowerManager pm;
	private PowerManager.WakeLock wakeLock;
	SeekBar seekBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_light);
		seekBar = (SeekBar) findViewById(R.id.seekbar);
		seekBar.setMax(MAX_LIGHT);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				int brightness = progress;
				if (brightness < MIN_LIGHT) {
					brightness = MIN_LIGHT;
				}
				processBrightness(brightness);
			}
		});

	}

	@Override
	protected void onResume() {
		lightOn();
		super.onResume();
	}

	@Override
	protected void onPause() {
		lightOut();
		super.onPause();
	}

	private void lightOn() {
		Log.i(TAG, "light on");
		pm = (PowerManager) getSystemService(POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
				"screen light");
		wakeLock.acquire();
	}

	private void lightOut() {
		if (wakeLock != null) {
			Log.i(TAG, "light out");
			wakeLock.release();
			wakeLock = null;
		}
	}

	private void processBrightness(int brightness) {
		LayoutParams lp = getWindow().getAttributes();
		lp.screenBrightness = brightness / 255.0f;
		getWindow().setAttributes(lp);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.screen_light, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_quit) {
			lightOut();
			finish();
			System.exit(0);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Toast.makeText(this, R.string.quit_tips, Toast.LENGTH_LONG).show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}

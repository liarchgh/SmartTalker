package com.neo.mytalker.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.neo.mytalker.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		autoJump();
	}
	
	public void autoJump() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent myintent = new Intent();
				myintent.setClass(MainActivity.this, FeatureShowActivity.class);
				startActivity(myintent);
				MainActivity.this.finish();
			}
		}, 1000*2);
	}
}

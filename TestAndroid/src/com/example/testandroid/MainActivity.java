package com.example.testandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView tv = null;
	EditText et = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = findViewById(R.id.tv);
		et = findViewById(R.id.et);
	}
	
	public void click(View v) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub

				String mn = "辣妹子";
				MusicEntity me = MusicManager.searchMusicInNetease(mn).get(0);
				final String res = me.getAlbumName();
//				MusicManager.musicPlay(MainActivity.this, MusicManager.searchMusicInNetease(mn).get(0));
				me.play(MainActivity.this);
				tv.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						tv.setText(res);
					}
				});
			}
		}).start();
	}
	public void pause(View v) {
		MusicManager.musicStop(MainActivity.this);
	}
	public void cc(View v) {
		MusicManager.musicContinue();
	}
}

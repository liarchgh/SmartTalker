package com.example.testandroid;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView tv = null;
	EditText et = null;
	ImageView iv = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = findViewById(R.id.tv);
		et = findViewById(R.id.et);
		iv = findViewById(R.id.iv);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = "http://p1.music.126.net/QXaISOoIt53f1nJFav_WEQ==/3312828537194189.jpg";
				final Bitmap bm = NetUtil.doGetBitmap(url, null);
				iv.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						iv.setImageBitmap(bm);
					}
				});
			}
		}).start();
	}
	
	public void click(View v) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub

				String mn = et.getText().toString();
				MusicEntity me = MusicManager.searchMusicInNetease(mn).get(0);
				final String res = me.getAlbumName();
//				MusicManager.musicPlay(MainActivity.this, MusicManager.searchMusicInNetease(mn).get(0));
				me.play(MainActivity.this);
			}
		}).start();
	}
	public void list(View v) {
		List<MusicEntity>lls = MusicManager.getMusicHistory();
		String res = "";
		for(int i = 0; i < lls.size(); ++i) {
			res = res + lls.get(i).getMusicName()+"\n";
		}
		tv.setText(res);
	}
	public void pause(View v) {
		MusicManager.musicStop(MainActivity.this);
	}
	public void cc(View v) {
		MusicManager.musicContinue();
	}
	public void pp(View v) {
		MusicManager.musicPrevious(MainActivity.this);
	}
	public void nn(View v) {
		MusicManager.musicNext(MainActivity.this);
	}
}

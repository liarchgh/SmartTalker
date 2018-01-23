package com.neo.mytalker.activity;

import java.io.IOException;

import com.neo.mytalker.R;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

public class Test extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//Log.i("music", "start");
//Log.i("music", GetMusicUrl.getSongUrl("life is like a boat"));
//Log.i("music", "end");
//			}
//		}).start();
		String musicUrl = "http://zhangmenshiting.qianqian.com/data2"+
			"/music/0d747e1c95ddad640b430902bc242823/290675782/"+
			"290675782.mp3?xcode=1194fc59527d5b99265fb8d128cb5641";
		MediaPlayer mp = new MediaPlayer();

		try {
			mp.setDataSource(musicUrl);

			mp.prepare();

			mp.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

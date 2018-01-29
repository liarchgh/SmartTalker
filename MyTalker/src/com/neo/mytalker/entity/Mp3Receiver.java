package com.neo.mytalker.entity;

import com.neo.mytalker.activity.ChatActivity;
import com.neo.mytalker.util.MusicManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Mp3Receiver extends BroadcastReceiver {  
    //private MusicManager application;  
  
    @Override  
    public void onReceive(Context context, Intent intent) {  
          
        //application = (Mp3Application) context.getApplicationContext();  
        String ctrl_code = intent.getAction();//获取action标记，用户区分点击事件  
          
        //music = application.music;//获取全局播放控制对象，该对象已在Activity中初始化  
		if ("play".equals(ctrl_code)) {
			MusicManager.musicContinue(context);
			final MusicEntity me = MusicManager.getMusicNow();
			((ChatActivity)context).initNotificationBar(
				me.getMusicName(), me.getAlbumImage());
		} else if ("pause".equals(ctrl_code)) {  
			MusicManager.musicStop(context);
		} else if ("next".equals(ctrl_code)) {  
			MusicManager.musicNext(context);
		} else if ("last".equals(ctrl_code)) {  
			MusicManager.musicPrevious(context);
		}  
    }  
  
}  
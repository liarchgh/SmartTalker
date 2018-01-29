package com.neo.mytalker.entity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Mp3Receiver extends BroadcastReceiver {  
    //private MusicManager application;  

  
    @Override  
    public void onReceive(Context context, Intent intent) {  
          
        //application = (Mp3Application) context.getApplicationContext();  
        String ctrl_code = intent.getAction();//获取action标记，用户区分点击事件  
//          
//        //music = application.music;//获取全局播放控制对象，该对象已在Activity中初始化  
//        if (music != null) {  
//            if ("play".equals(ctrl_code)) {  
//                music.moveon();  
//                System.out.println("play");  
//            } else if ("pause".equals(ctrl_code)) {  
//                music.pause();  
//                System.out.println("pause");  
//            } else if ("next".equals(ctrl_code)) {  
//                music.nextSong();  
//                System.out.println("next");  
//            } else if ("last".equals(ctrl_code)) {  
//                music.lastSong();  
//                System.out.println("last");  
//            }  
//        }  
//  
//        if ("cancel".equals(ctrl_code)) {  
//            application.notManager.cancel(Const.NOTI_CTRL_ID);  
//            System.exit(0);  
//        }  
    }  
  
}  
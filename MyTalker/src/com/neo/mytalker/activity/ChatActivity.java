package com.neo.mytalker.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.neo.mytalker.R;
import com.neo.mytalker.entity.GlobalSettings;
import com.neo.mytalker.fragments.ChatBarFragment;
import com.neo.mytalker.fragments.ChatMenuFragment;
import com.neo.mytalker.fragments.ChatRecordFragment;
import com.neo.mytalker.myinterface.ThemeInterface;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

//TODO:Remove this to enable UNITY
public class ChatActivity extends UnityPlayerActivity {
// public class ChatActivity extends Activity {
	public ChatRecordFragment mChatRecFrag;
	public ChatBarFragment mChatBarFrag;
	public ChatMenuFragment mChatMenuFragment;
	public List<ThemeInterface> mThemeList;
	private View mRootView, mUnityView;
	private int mScreenHeight;
	private int mkeyHeight;
	public boolean isQuitting;

	public boolean SIMPLE_MODE = false;// TODO:turn off to enable Unity.
	private int currentQualityLevel, oldQualityLevel;
	private SharedPreferences mSharedPreferences;
	private TextView mText;
	private String[] mQualityStrings = { "极低画质", "低画质", "中画质", "高画质", "超高画质", "极高画质", };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		mRootView = findViewById(R.id.chat_root);

		mThemeList = new ArrayList<ThemeInterface>();
		mChatRecFrag = new ChatRecordFragment(this);
		mChatBarFrag = new ChatBarFragment(this);
		mChatMenuFragment = new ChatMenuFragment(this);

		SetFragment(R.id.chat_recordfrag, mChatRecFrag);
		SetFragment(R.id.chat_bottombarfrag, mChatBarFrag);
		SetFragment(R.id.message_plus_fragment, mChatMenuFragment);

		mThemeList.add(mChatBarFrag);
		mThemeList.add(mChatMenuFragment);
		mThemeList.add(mChatRecFrag);

		Toast.makeText(this, "" + String.format("#%06X", 0xFFFFFF & GlobalSettings.THEME_COLOR), Toast.LENGTH_SHORT)
				.show();

		setAnimation();

		initNotificationBar();
		// TODO:Remove this to enable UNITY
		mSharedPreferences = getSharedPreferences("AppCountss", MODE_PRIVATE);
		currentQualityLevel = mSharedPreferences.getInt("Quality", 2);
		
		if (!SIMPLE_MODE) {
		LinearLayout ll = (LinearLayout) findViewById(R.id.chat_unityview);
		mUnityView = mUnityPlayer.getView();
		ll.addView(mUnityView);
		}

	}

	private void SetFragment(int content, Fragment frag) {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(content, frag);
		transaction.commit();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		hideKeyboardOnStartup();
		mSharedPreferences = getSharedPreferences("AppCountss", MODE_PRIVATE);
		GlobalSettings.THEME_COLOR = mSharedPreferences.getInt("ThemeColor", Color.parseColor("#66ccff"));
		LoadThemeColor();

	}

	public void setAnimation() {
		View logo = findViewById(R.id.chat_splash);
		ObjectAnimator animator1 = ObjectAnimator.ofFloat(logo, "alpha", 1f, 0f);
		animator1.setDuration(1000);
		animator1.start();
	}

	public void hideKeyboardOnStartup() {
		TextView config_hidden = (TextView) this.findViewById(R.id.config_hidden);
		config_hidden.requestFocus();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
			if (!isQuitting) {
				Toast.makeText(getApplicationContext(), "再次按下以退出", Toast.LENGTH_SHORT).show();
				isQuitting = true;
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						isQuitting = false;
					}
				}, 3000);
				return true;
			} else {
				finish();
				return true;
			}
		} else {
			return super.dispatchKeyEvent(event);
		}
	}

	public void Speak() {

		// TODO:Remove this to enable UNITY

		if (!SIMPLE_MODE) {
		UnityPlayer.UnitySendMessage("miku", "Speak", "");
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
		
		@Override
		public void run() { // TODO Auto-generated method stub
		UnityPlayer.UnitySendMessage("miku", "Idle", "");
		}
		}, 1000);
		}

	}

	public void Dance() {
		// TODO:Remove this to enable UNITY

		if (!SIMPLE_MODE) {
		View tmp = findViewById(R.id.chat_cover);
		tmp.setVisibility(View.VISIBLE);
		ObjectAnimator animator1 = ObjectAnimator.ofFloat(tmp, "alpha", 1f, 0f);
		
		animator1.setDuration(1000);
		animator1.start();
		UnityPlayer.UnitySendMessage("miku", "Dance", "");
		}

	}

	public int getCurrentQualityLevel() {

		return currentQualityLevel;
	}

	public void SetQuality(int l) {
		// TODO:Remove this to enable UNITY

		if (!SIMPLE_MODE) {
		UnityPlayer.UnitySendMessage("miku", "SetQualityLevel", String.valueOf(l));
		currentQualityLevel = l;
		}

	}

	public void AutoAdjust(int maxLevel, TextView txt) {
		// TODO:Remove this to enable UNITY

		if (!SIMPLE_MODE) {
		mText=txt;
		UnityPlayer.UnitySendMessage("miku", "AutoDetectQuality",
		String.valueOf(maxLevel));
		Toast.makeText(this, "正在自动调整画质，请稍后", Toast.LENGTH_SHORT).show();
		}

	}

	public void setCurrentQualityLevel(int l) {
		Toast.makeText(this, "画质调整完成:" + mQualityStrings[l], Toast.LENGTH_SHORT).show();
		Log.i("ZX", "Current Quality:" + l);

		currentQualityLevel = l;
		TextView mText = (TextView) mChatMenuFragment.mQualitySettingsDialog.findViewById(R.id.quality_now);
		mText.setText(mQualityStrings[l]);
	}

	public void SaveOldQualityLevel() {
		oldQualityLevel = currentQualityLevel;
	}

	public void SaveQualityLevel() {
		mSharedPreferences = getSharedPreferences("AppCountss", MODE_PRIVATE);
		Editor editor = mSharedPreferences.edit();
		editor.putInt("Quality", currentQualityLevel);
		editor.commit();
	}

	public void RevertQualityLevel() {
		SetQuality(oldQualityLevel);
	}

	public void ChangeThemeColor(String color) {

		GlobalSettings.THEME_COLOR = Color.parseColor(color);
		mSharedPreferences = getSharedPreferences("AppCountss", MODE_PRIVATE);
		Editor editor = mSharedPreferences.edit();
		editor.putInt("ThemeColor", GlobalSettings.THEME_COLOR);
		editor.commit();
		LoadThemeColor();
	}
	public void ChangeThemeColor(int color) {

		GlobalSettings.THEME_COLOR = color;
		mSharedPreferences = getSharedPreferences("AppCountss", MODE_PRIVATE);
		Editor editor = mSharedPreferences.edit();
		editor.putInt("ThemeColor", GlobalSettings.THEME_COLOR);
		editor.commit();
		LoadThemeColor();
	}
	public void LoadThemeColor() {

		for (ThemeInterface t : mThemeList) {
			t.ChangeThemeColor();
		}
	}

	public void UnityReady(String params) {
		Toast.makeText(this, "UnityIsReady", Toast.LENGTH_SHORT).show();
		setCurrentQualityLevel(currentQualityLevel);

	}

	private RemoteViews contentView;

	private Notification notification;
	public NotificationManager notManager; 
	public void initNotificationBar() {
		notManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE); 
		Notification.Builder mBuilder = new Notification.Builder(this);
		
		notification = new Notification();
		// 初始化通知
		notification.icon = R.drawable.iclauncher;
		contentView = new RemoteViews(getPackageName(), R.layout.notification_control);
		notification.contentView = contentView;
		Intent intentPlay = new Intent("play");
		PendingIntent pIntentPlay = PendingIntent.getBroadcast(this, 0, intentPlay, 0);
		contentView.setOnClickPendingIntent(R.id.music_isplaying, pIntentPlay);// 为play控件注册事件


		Intent intentNext = new Intent("next");
		PendingIntent pIntentNext = PendingIntent.getBroadcast(this, 0, intentNext, 0);
		contentView.setOnClickPendingIntent(R.id.music_next, pIntentNext);

		Intent intentLast = new Intent("last");
		PendingIntent pIntentLast = PendingIntent.getBroadcast(this, 0, intentLast, 0);
		contentView.setOnClickPendingIntent(R.id.music_prev, pIntentLast);

		notification.flags = notification.FLAG_NO_CLEAR;// 设置通知点击或滑动时不被清除
		notification.bigContentView = contentView;
		notManager.notify(32000, notification);// 开启通知

	}

}

﻿package com.neo.mytalker.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.neo.mytalker.R;
import com.neo.mytalker.entity.AndroidBug5497Workaround;
import com.neo.mytalker.entity.GlobalSettings;
import com.neo.mytalker.entity.MusicEntity;
import com.neo.mytalker.fragments.ChatBarFragment;
import com.neo.mytalker.fragments.ChatMenuFragment;
import com.neo.mytalker.fragments.ChatRecordFragment;
import com.neo.mytalker.myinterface.MikuProgressDialog;
import com.neo.mytalker.myinterface.ThemeInterface;
import com.neo.mytalker.util.MusicManager;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

//TODO:Remove this to enable UNITY
public class ChatActivity extends UnityPlayerActivity {
//	 public class ChatActivity extends Activity {
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
	private TextView mText, mTextDialog;
	private String[] mQualityStrings = { "极低画质", "低画质", "中画质", "高画质", "超高画质", "极高画质", };
	private Mp3Receiver mMp3Receiver;
	private MikuProgressDialog mMikuProgressDialog;
	private View mLoadingAnim;
	private ImageView mImageView;
	private AnimationDrawable mAnimationDrawable;
	public boolean isGyroOn, oldGyroState, oldSimpleMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		//
		// }
		// });

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

		// Toast.makeText(this, "" + String.format("#%06X", 0xFFFFFF &
		// GlobalSettings.THEME_COLOR), Toast.LENGTH_SHORT).show();

		setAnimation();
		mMp3Receiver = new Mp3Receiver();
		IntentFilter inf = new IntentFilter();
		inf.addAction("play");
		inf.addAction("pause");
		inf.addAction("next");
		inf.addAction("last");
		registerReceiver(mMp3Receiver, inf);
		// TODO:Remove this to enable UNITY
		mSharedPreferences = getSharedPreferences("AppCountss", MODE_PRIVATE);
		currentQualityLevel = mSharedPreferences.getInt("Quality", 2);
		isGyroOn = true;
		if (!SIMPLE_MODE) {
			LinearLayout ll = (LinearLayout) findViewById(R.id.chat_unityview);
			mUnityView = mUnityPlayer.getView();
			ll.addView(mUnityView);
		}

		AndroidBug5497Workaround.assistActivity(this);

		mLoadingAnim = findViewById(R.id.chat_loadinganim);
//		mLoadingAnim.setVisibility(View.GONE);
		mLoadingAnim.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub

				return true;
			}
		});

		mTextDialog = (TextView) findViewById(R.id.chat_loading_text);
		mTextDialog.setText("正在加载机器人...");
		mImageView = (ImageView) findViewById(R.id.chat_loading_img);
		mImageView.setBackgroundResource(R.anim.miku_frame);
		mAnimationDrawable = (AnimationDrawable) mImageView.getBackground();
		mAnimationDrawable.start();

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
		// Log.i("speak", "start");

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
			mText = txt;
			UnityPlayer.UnitySendMessage("miku", "AutoDetectQuality", String.valueOf(maxLevel));
			Toast.makeText(this, "正在自动调整画质，请稍后", Toast.LENGTH_SHORT).show();
		}

	}

	public void setCurrentQualityLevel(int l) {
		Toast.makeText(this, "画质调整完成:" + mQualityStrings[l], Toast.LENGTH_SHORT).show();
		// Log.i("ZX", "Current Quality:" + l);

		currentQualityLevel = l;
		new Thread(new Runnable() {

			@Override
			public void run() {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (mText != null) {
							TextView mText = (TextView) mChatMenuFragment.mSettingDialog.mQualitySettingsDialog
									.findViewById(R.id.quality_now);
							mText.setText(mQualityStrings[currentQualityLevel]);
						}
					}
				});

			}
		}).start();

	}

	public void DirectSetCurrentQualityLevel(int l) {
		Toast.makeText(this, "画质调整完成:" + mQualityStrings[l], Toast.LENGTH_SHORT).show();
		// Log.i("ZX", "Current Quality:" + l);

		currentQualityLevel = l;

	}

	public void SaveOldQualityLevel() {
		oldQualityLevel = currentQualityLevel;
		oldGyroState = isGyroOn;
		oldSimpleMode = SIMPLE_MODE;
	}

	public void SaveQualityLevel() {
		mSharedPreferences = getSharedPreferences("AppCountss", MODE_PRIVATE);
		Editor editor = mSharedPreferences.edit();
		editor.putInt("Quality", currentQualityLevel);
		editor.commit();
	}

	public void RevertQualityLevel() {
		SetQuality(oldQualityLevel);
		if (isGyroOn != oldGyroState) {
			ToggleGyro("");
		}
		if (oldSimpleMode != SIMPLE_MODE) {
			ToggleUnity();
		}
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

	public void ToggleGyro(String args) {
		// TODO:Remove this to enable UNITY
		if (!SIMPLE_MODE) {
			UnityPlayer.UnitySendMessage("miku", "ToogleGyro", "");
			isGyroOn = !isGyroOn;
		}
	}

	public void ToggleUnity() {
		SIMPLE_MODE = !SIMPLE_MODE;
		if (SIMPLE_MODE) {
			mUnityView.setVisibility(View.GONE);
		} else {
			mUnityView.setVisibility(View.VISIBLE);
		}
	}

	public void LoadThemeColor() {

		for (ThemeInterface t : mThemeList) {
			t.ChangeThemeColor();
		}
	}

	public void UnityReady(String params) {
		// Toast.makeText(this, "UnityIsReady", Toast.LENGTH_SHORT).show();

		DirectSetCurrentQualityLevel(currentQualityLevel);
		new Thread(new Runnable() {

			@Override
			public void run() {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mImageView.clearAnimation();
						mLoadingAnim.setVisibility(View.GONE);
					}
				});

			}
		}).start();
	}

	private RemoteViews contentView;

	private Notification notification;
	public NotificationManager notManager;
	private Bitmap bm = null;
	private String name = null;

	public void initNotificationBar() {
		notManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification.Builder mBuilder = new Notification.Builder(this);

		notification = new Notification();
		// 初始化通知
		notification.icon = R.drawable.iclauncher;
		contentView = new RemoteViews(getPackageName(), R.layout.notification_control);
		notification.contentView = contentView;
		// Log.i("ZX",""+MusicManager.musicIsPlaying());
		if (!MusicManager.musicIsPlaying()) {
			Intent intentPlay = new Intent("play");
			PendingIntent pIntentPlay = PendingIntent.getBroadcast(this, 0, intentPlay, 0);

			contentView.setImageViewBitmap(R.id.music_state,
					BitmapFactory.decodeResource(getResources(), R.drawable.ic_play_arrow_white_48dp));

			contentView.setOnClickPendingIntent(R.id.music_state, pIntentPlay);// 为play控件注册事件
		} else {
			Intent intentPlay = new Intent("pause");
			PendingIntent pIntentPlay = PendingIntent.getBroadcast(this, 0, intentPlay, 0);

			contentView.setImageViewBitmap(R.id.music_state,
					BitmapFactory.decodeResource(getResources(), R.drawable.ic_pause_white_48dp));
			contentView.setOnClickPendingIntent(R.id.music_state, pIntentPlay);// 为play控件注册事件
		}
		contentView.setImageViewBitmap(R.id.music_cover, bm);
		contentView.setTextViewText(R.id.music_title, name);

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

	public void initNotificationBar(String name, Bitmap cover) {
		this.name = name;
		if (cover == null) {
			bm = cover = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
		} else {
			bm = cover;
		}
		notManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification.Builder mBuilder = new Notification.Builder(this);

		notification = new Notification();
		// 初始化通知
		notification.icon = R.drawable.iclauncher;
		contentView = new RemoteViews(getPackageName(), R.layout.notification_control);
		notification.contentView = contentView;
		// Log.i("ZX",""+MusicManager.musicIsPlaying());
		if (!MusicManager.musicIsPlaying()) {
			Intent intentPlay = new Intent("play");
			PendingIntent pIntentPlay = PendingIntent.getBroadcast(this, 0, intentPlay, 0);

			contentView.setImageViewBitmap(R.id.music_state,
					BitmapFactory.decodeResource(getResources(), R.drawable.ic_play_arrow_white_48dp));

			contentView.setOnClickPendingIntent(R.id.music_state, pIntentPlay);// 为play控件注册事件
		} else {
			Intent intentPlay = new Intent("pause");
			PendingIntent pIntentPlay = PendingIntent.getBroadcast(this, 0, intentPlay, 0);

			contentView.setImageViewBitmap(R.id.music_state,
					BitmapFactory.decodeResource(getResources(), R.drawable.ic_pause_white_48dp));
			contentView.setOnClickPendingIntent(R.id.music_state, pIntentPlay);// 为play控件注册事件
		}
		contentView.setImageViewBitmap(R.id.music_cover, cover);
		contentView.setTextViewText(R.id.music_title, name);

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

	public class Mp3Receiver extends BroadcastReceiver {
		// private MusicManager application;

		@Override
		public void onReceive(final Context context, Intent intent) {

			// application = (Mp3Application) context.getApplicationContext();
			String ctrl_code = intent.getAction();// 获取action标记，用户区分点击事件

			// final MusicEntity me = MusicManager.getMusicNow();
			// music = application.music;//获取全局播放控制对象，该对象已在Activity中初始化
			if ("play".equals(ctrl_code)) {
				MusicManager.musicContinue(ChatActivity.this);
			} else if ("pause".equals(ctrl_code)) {
				MusicManager.musicStop(ChatActivity.this);
			} else if ("next".equals(ctrl_code)) {
				MusicEntity next = MusicManager.getNextMusic(ChatActivity.this);
				if (next != null) {
					// loadMusic(me, context);
					MusicManager.musicNext(ChatActivity.this);
				}
			} else if ("last".equals(ctrl_code)) {
				MusicEntity previous = MusicManager.getPreviousMusic(ChatActivity.this);
				if (previous != null) {
					// loadMusic(me, context);
					MusicManager.musicPrevious(ChatActivity.this);
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
		if (contentView != null) {
			notManager.cancel(32000);
		}
		if (mMp3Receiver != null) {
			unregisterReceiver(mMp3Receiver);
		}
	}
}

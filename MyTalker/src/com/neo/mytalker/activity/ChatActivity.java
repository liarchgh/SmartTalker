package com.neo.mytalker.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.neo.mytalker.R;
import com.neo.mytalker.fragments.ChatBarFragment;
import com.neo.mytalker.fragments.ChatMenuFragment;
import com.neo.mytalker.fragments.ChatRecordFragment;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//TODO:Remove this to enable UNITY
public class ChatActivity extends UnityPlayerActivity implements OnLayoutChangeListener {
	// public class ChatActivity extends Activity implements OnLayoutChangeListener
	// {
	public ChatRecordFragment mChatRecFrag;
	public ChatBarFragment mChatBarFrag;
	public ChatMenuFragment mChatMenuFragment;
	private View mRootView;
	private int mScreenHeight;
	private int mkeyHeight;
	public boolean isQuitting, isKeyboardOn = false;

	private boolean SIMPLE_MODE = false;// TODO:turn off to enable Unity.

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		mRootView = findViewById(R.id.chat_root);
		mScreenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
		mkeyHeight = mScreenHeight / 3;
		mChatRecFrag = new ChatRecordFragment(this);

		mChatBarFrag = new ChatBarFragment(this);
		mChatMenuFragment = new ChatMenuFragment();
		SetFragment(R.id.chat_recordfrag, mChatRecFrag);
		SetFragment(R.id.chat_bottombarfrag, mChatBarFrag);
		SetFragment(R.id.message_plus_fragment, mChatMenuFragment);

		setAnimation();
		// TODO:Remove this to enable UNITY

		if (!SIMPLE_MODE) {
			LinearLayout ll = (LinearLayout) findViewById(R.id.chat_unityview);
			View myview = mUnityPlayer.getView();
			ll.addView(myview);
		}

	}

	private void SetFragment(int content, Fragment frag) {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(content, frag);
		transaction.commit();
	}

	@Override
	public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight,
			int oldBottom) {
		// TODO Auto-generated method stub
		if (bottom != 0 && oldBottom != 0 && oldBottom - bottom > mkeyHeight) {
			isKeyboardOn = true;
			mChatMenuFragment.ToggleMenu(!isKeyboardOn);
			Toast.makeText(this, "键盘弹起", Toast.LENGTH_SHORT).show();
		} else if (bottom != 0 && oldBottom != 0 && bottom - oldBottom > mkeyHeight) {
			isKeyboardOn = false;
			mChatMenuFragment.ToggleMenu(!isKeyboardOn);
			Toast.makeText(this, "键盘收起", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mRootView.addOnLayoutChangeListener(this);
		hideKeyboardOnStartup();
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
				Toast.makeText(getApplicationContext(), "Back again to quit.", Toast.LENGTH_SHORT).show();
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
}

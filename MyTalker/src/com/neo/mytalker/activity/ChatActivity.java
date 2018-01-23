package com.neo.mytalker.activity;

import com.neo.mytalker.R;
import com.neo.mytalker.fragments.ChatBarFragment;
import com.neo.mytalker.fragments.ChatMenuFragment;
import com.neo.mytalker.fragments.ChatRecordFragment;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.Window;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class ChatActivity extends FragmentActivity implements OnLayoutChangeListener{
	public ChatRecordFragment mChatRecFrag;
	public ChatBarFragment mChatBarFrag;
	public ChatMenuFragment mChatMenuFragment;
	private View mRootView;
	private int mScreenHeight;
	private int mkeyHeight;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);
		
		mRootView = findViewById(R.id.chat_root);
		mScreenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
		mkeyHeight = mScreenHeight/3;
		mChatRecFrag=new ChatRecordFragment(this);
		
		mChatBarFrag=new ChatBarFragment(this);
		mChatMenuFragment = new ChatMenuFragment();
		SetFragment(R.id.chat_recordfrag, mChatRecFrag);
		SetFragment(R.id.chat_bottombarfrag,mChatBarFrag);
		SetFragment(R.id.message_plus_fragment, mChatMenuFragment);
		
		
	}
	
	private void SetFragment(int content, Fragment frag) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(content, frag);
		transaction.commit();
	}

	@Override
	public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight,
			int oldBottom) {
		// TODO Auto-generated method stub
		if(bottom != 0 && oldBottom != 0 && oldBottom-bottom > mkeyHeight) {
			Toast.makeText(this, "¼üÅÌµ¯Æğ", Toast.LENGTH_SHORT).show();
		}else if(bottom != 0 && oldBottom != 0 && bottom-oldBottom > mkeyHeight){
			Toast.makeText(this, "¼üÅÌÊÕÆğ", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mRootView.addOnLayoutChangeListener(this);
	}
	
	
	
}

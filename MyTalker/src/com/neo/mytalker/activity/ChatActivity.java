package com.neo.mytalker.activity;

import com.neo.mytalker.R;
import com.neo.mytalker.fragments.ChatBarFragment;
import com.neo.mytalker.fragments.ChatMenuFragment;
import com.neo.mytalker.fragments.ChatRecordFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

public class ChatActivity extends FragmentActivity{
	public ChatRecordFragment mChatRecFrag;
	public ChatBarFragment mChatBarFrag;
	
	private ChatMenuFragment mChatMenuFragment;
	private Context mChatMenuFragmentContext;
	View mMessagePlusFun;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		mChatRecFrag=new ChatRecordFragment(this);
		mChatBarFrag=new ChatBarFragment(this);
		SetFragment(R.id.chat_recordfrag, mChatRecFrag);
		SetFragment(R.id.chat_bottombarfrag,mChatBarFrag);

	}
	
	private void SetFragment(int content, Fragment frag) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(content, frag);
		transaction.commit();
	}
	
	/*public void initFragment() {
		mChatMenuFragment = (ChatMenuFragment) getSupportFragmentManager().findFragmentById(R.id.activity_chat_menu);
		
	}

	public void initView() {
		mMessagePlusFun = mChatMenuFragment.getView(); 
	}
	
	public void switchBtn(View view) {
		switch(view.getId()) {
			case R.id.chat_bottombar_send:
				break;
			case R.id.chat_bottombar_morefun:
				SetFragment(R.id.message_plus_fragment, mChatMenuFragment);
				if(mMessagePlusFun.getVisibility() == View.VISIBLE) {
					
				}
				break;
		}
	}*/
	
}

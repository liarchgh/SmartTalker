package com.neo.mytalker.fragments;

import com.neo.mytalker.R;
import com.neo.mytalker.activity.ChatActivity;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ChatBarFragment extends Fragment {

	private View mRoot;
	private Context mContext;
	private TextView mMore,mSend,mText;
	private ChatActivity mChatActivity;
	private ChatRecordFragment mChatRecFrag;
	public ChatBarFragment(ChatActivity activity)
	{
		mChatActivity=activity;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

			mRoot = inflater.inflate(R.layout.fragment_chat_bar, container, false);
			mContext = container.getContext();
			InitBarBtns();
			return mRoot;
	}
	private void InitBarBtns()
	{
		mMore=(TextView) mRoot.findViewById(R.id.chat_bottombar_morefun);
		mMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		
		});
		
		
		
		mText=(TextView) mRoot.findViewById(R.id.chat_bottombar_sendingtext);
		
		mChatRecFrag=mChatActivity.mChatRecFrag;
		mSend=(TextView) mRoot.findViewById(R.id.chat_bottombar_send);
		mSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String tmp=mText.getText().toString();
				if(!tmp.equals(""))
				{
					mChatRecFrag.AddRecord(true, tmp);
					mText.setText("");
				}else {
					Toast.makeText(mChatActivity, "Type to continue...", Toast.LENGTH_SHORT).show();
				}
			}
		
		});
		
	}
	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return mRoot;
	}
	
}

package com.neo.mytalker.fragments;

import com.neo.mytalker.R;
import com.neo.mytalker.activity.ChatActivity;
import com.neo.mytalker.util.ChatWithTalker;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ChatBarFragment extends Fragment {

	private View mRoot, mChatActivityView;
	private Context mContext;
	private TextView mMore,mSend,mText;
	private ChatActivity mChatActivity;
	private ChatRecordFragment mChatRecFrag;
	private ChatMenuFragment mChatMenuFragment;
	private LayoutInflater mLayoutInflater;
	private ViewGroup mChatActivityViewGroup;

	public ChatBarFragment(ChatActivity activity)
	{
		mChatActivity=activity;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

			mLayoutInflater = inflater;
			mChatActivityView = inflater.inflate(R.layout.activity_chat, null, false);
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
				if(mChatActivity.findViewById(R.id.message_plus_fragment).getVisibility() == View.GONE) {
					mChatActivity.findViewById(R.id.message_plus_fragment).setVisibility(View.VISIBLE);
					Toast.makeText(mContext, "GONE", Toast.LENGTH_LONG).show();
				}else {
					mChatActivity.findViewById(R.id.message_plus_fragment).setVisibility(View.GONE);
					Toast.makeText(mContext, "VISIBLE", Toast.LENGTH_LONG).show();
				}
				
			}
		
		});
		
		mChatMenuFragment=mChatActivity.mChatMenuFragment;
		
		mText=(TextView) mRoot.findViewById(R.id.chat_bottombar_sendingtext);
		
		mChatRecFrag=mChatActivity.mChatRecFrag;
		mSend=(TextView) mRoot.findViewById(R.id.chat_bottombar_send);
		mSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//get string to send to TR
				String tmp=mText.getText().toString();
				if(!tmp.equals(""))
				{
					mChatRecFrag.AddRecord(true, tmp);
					mText.setText("");
					//to receive from TR
					
					//TODO:Modify the result to update
					new ChatWithTalker(mChatRecFrag, mContext, 0, tmp).execute();
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

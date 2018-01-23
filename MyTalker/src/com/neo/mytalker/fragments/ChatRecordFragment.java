package com.neo.mytalker.fragments;

import java.util.ArrayList;
import java.util.Date;

import com.neo.mytalker.R;
import com.neo.mytalker.activity.ChatActivity;
import com.neo.mytalker.adapter.ChatRecordAdapter;
import com.neo.mytalker.impl.ChatRecordData;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ChatRecordFragment extends Fragment {
	private ArrayList<ChatRecordData> mChatRecordData;
	private View mRoot;
	private Context mContext;
	private ChatActivity mChatActivity;
	private ChatRecordAdapter mChatRecordAdapter;
	private static int LIST_MIN_PART_CNT = 2, LIST_MAX_PART_CNT = 4;
	private boolean isMaximized = false, isScrolling = false;
	public ChatRecordFragment(ChatActivity activity)
	{
		mChatActivity=activity;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mRoot = inflater.inflate(R.layout.fragment_chat_record, container, false);
		mContext = container.getContext();
		InitChatList();
		return mRoot;
	}

	private void InitChatList() {
		if (mChatRecordData == null) {
			mChatRecordData = new ArrayList<ChatRecordData>();
		}
		InitChatCardData();
		ListView ls = (ListView) mRoot.findViewById(R.id.chat_msglist);
		mChatRecordAdapter = new ChatRecordAdapter((Activity) mContext, mChatRecordData,
				R.layout.listpart_chat_record);
		ls.setAdapter(mChatRecordAdapter);
		ls.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				if (isMaximized) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_MOVE:
						isScrolling = true;
						//Log.i("ZX", "Move");
						return false;
					case MotionEvent.ACTION_UP:
						if (!isScrolling) {
							setListViewHeightBasedOnChildren((ListView) v, LIST_MIN_PART_CNT);
							isMaximized = false;
						}
						isScrolling = false;
						//Log.i("ZX", "Up");
						return false;
					default:
						break;
					}
					return false;
				} else {
					switch (event.getAction()) {
					case MotionEvent.ACTION_SCROLL:
						return true;
					case MotionEvent.ACTION_UP:
						setListViewHeightBasedOnChildren((ListView) v, LIST_MAX_PART_CNT);
						isMaximized = true;
					default:
						break;
					}
					return true;
				}

			}

		});
		setListViewHeightBasedOnChildren(ls, LIST_MIN_PART_CNT);
		ls.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if (isMaximized) {
					if (!isScrolling) {
						setListViewHeightBasedOnChildren((ListView) parent, LIST_MIN_PART_CNT);
						isMaximized = false;
					}
				}
			}

		});
		Log.i("ZX", "setAdapter");
	}

	public void setListViewHeightBasedOnChildren(ListView listView, int cnt) {

		ChatRecordAdapter listAdapter = (ChatRecordAdapter) listView.getAdapter();
		isMaximized = false;
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;

		for (int i = listAdapter.getCount() - 1; i > listAdapter.getCount() - 1 - cnt; i--) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();

		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		// ((MarginLayoutParams) params).setMargins(10, 10, 10, 10); // ©ии╬ЁЩ

		listView.setLayoutParams(params);
	}

	public void InitChatCardData() {
		mChatRecordData = new ArrayList<ChatRecordData>();
		for (int i = 0; i < 100; i++) {
			ChatRecordData tmp = new ChatRecordData();
			tmp.msg = "testMsg" + i;
			tmp.time = new Date().getTime();
			tmp.isMe = (i / (int) (Math.random() * 100 + 1) % 2) == 0 ? true : false;
			mChatRecordData.add(tmp);
		}
		Log.i("ZX", "Init");
	}
	
	
	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return mRoot;
	}
	
	public void AddRecord(boolean isMine,String content)
	{
		ChatRecordData tmp = new ChatRecordData();
		tmp.msg = content;
		tmp.time = new Date().getTime();
		tmp.isMe = isMine;
		mChatRecordData.add(tmp);
		mChatRecordAdapter.notifyDataSetChanged();
	}
}

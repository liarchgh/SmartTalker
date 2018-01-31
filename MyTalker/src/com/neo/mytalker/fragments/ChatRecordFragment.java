package com.neo.mytalker.fragments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.neo.mytalker.R;
import com.neo.mytalker.activity.ChatActivity;
import com.neo.mytalker.adapter.ChatRecordAdapter;
import com.neo.mytalker.entity.ChatRecordData;
import com.neo.mytalker.entity.GlobalSettings;
import com.neo.mytalker.myinterface.ThemeInterface;
import com.neo.mytalker.util.AskAndAnswer;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ChatRecordFragment extends Fragment implements ThemeInterface {
	private ArrayList<ChatRecordData> mChatRecordData;
	private View mRoot;
	private Context mContext;
	public ChatActivity mChatActivity;
	private ChatRecordAdapter mChatRecordAdapter;
	private ListView mChatRecordListView;
	// private TextView loadingView = null;
	// private int mChatRecordData.size();
	private static int LIST_MIN_PART_CNT = 2, LIST_MAX_PART_CNT = 4;
	private boolean isMaximized = false, isScrolling = false;
	// 当前列表中最早记录的数据库中的id -1表示列表没有数据
	private int mEarliestId = -1;
	// 每次从数据库取出的历史记录的条数
	private int mQuerySize = 10;
	private boolean ifLoading = false;

	public ChatRecordFragment(ChatActivity activity) {
		mChatActivity = activity;
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
		queryHistory();
		mChatRecordListView = (ListView) mRoot.findViewById(R.id.chat_msglist);
		mChatRecordAdapter = new ChatRecordAdapter((Activity) mContext, mChatRecordData, R.layout.listpart_chat_record);
		mChatRecordListView.setAdapter(mChatRecordAdapter);
		mChatRecordListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				if (isMaximized) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_MOVE:
						isScrolling = true;
						// Log.i("ZX", "Move");
						return false;
					case MotionEvent.ACTION_UP:
						if (!isScrolling) {
							setListViewHeightBasedOnChildren((ListView) v, LIST_MIN_PART_CNT);
							isMaximized = false;
						}
						isScrolling = false;
						// Log.i("ZX", "Up");
						return false;
					default:
						break;
					}
					return false;
				} else {
					switch (event.getAction()) {
					case MotionEvent.ACTION_MOVE:
						isScrolling = true;
						// Log.i("ZX", "Move");
						return true;
					case MotionEvent.ACTION_CANCEL:
						isScrolling = false;
						return true;
					case MotionEvent.ACTION_UP:
						if (!isScrolling) {
							setListViewHeightBasedOnChildren((ListView) v, LIST_MAX_PART_CNT);
							isMaximized = true;
						}
						isScrolling = false;
						return true;
					case MotionEvent.ACTION_SCROLL:
						return true;
					default:
						break;
					}
					return true;
				}

			}

		});
		isMaximized = false;
		setListViewHeightBasedOnChildren(mChatRecordListView, LIST_MIN_PART_CNT);
		mChatRecordListView.setOnItemClickListener(new OnItemClickListener() {

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
	}

	public void setListViewHeightBasedOnChildren(ListView listView, int cnt) {

		ChatRecordAdapter listAdapter = (ChatRecordAdapter) listView.getAdapter();
		isMaximized = false;
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		int listCnt = 0;
		for (int i = listAdapter.getCount() - 1; i >= (listAdapter.getCount() - cnt > 0 ? listAdapter.getCount() - cnt
				: 0); i--) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
			listCnt++;
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();

		params.height = totalHeight + (listView.getDividerHeight() * (listCnt));

		// ((MarginLayoutParams) params).setMargins(10, 10, 10, 10); // ��ɾ��

		listView.setLayoutParams(params);
	}

	public void queryHistory() {
		// mChatRecordData = new ArrayList<ChatRecordData>();
		// for (int i = 0; i < 100; i++) {
		// ChatRecordData tmp = new ChatRecordData();
		// tmp.msg = "testMsg" + i;
		// tmp.time = new Date().getTime();
		// tmp.isMe = (i / (int) (Math.random() * 100 + 1) % 2) == 0 ? true : false;
		// mChatRecordData.add(tmp);
		// }

		List<Map<String, String>> sqlAnswer = new AskAndAnswer(mContext, 0).getHistory(mEarliestId, mQuerySize);
		ChatRecordData tCrd = null;
		Map<String, String> tempAns = null;
		for (int i = 0; sqlAnswer != null && i < sqlAnswer.size(); ++i) {
			tempAns = sqlAnswer.get(i);
			tCrd = new ChatRecordData();
			tCrd.isMe = true;
			tCrd.msg = tempAns.get(AskAndAnswer.askName);
			mChatRecordData.add(tCrd);

			tCrd = new ChatRecordData();
			tCrd.isMe = false;
			tCrd.msg = tempAns.get(AskAndAnswer.answerName);
			mChatRecordData.add(tCrd);
		}
	}

	public void AddRecord(boolean isMine, String content) {
		stopLoading();
		ChatRecordData tmp = new ChatRecordData();
		tmp.msg = content;
		tmp.time = new Date().getTime();
		tmp.isMe = isMine;
		// mChatRecordData.add(tmp);
		// mChatRecordData.add(lastPosition=mChatRecordData.size(), tmp);
		mChatRecordData.add(tmp);
		mChatRecordAdapter.notifyDataSetChanged();
		setListViewHeightBasedOnChildren(mChatRecordListView, isMaximized ? LIST_MAX_PART_CNT : LIST_MIN_PART_CNT);
//		SetSpeak();
	}
	//
	// public void addItem(boolean isMine,String content) {
	// stopLoading();
	// ChatRecordData tmp = new ChatRecordData();
	// tmp.msg = content;
	// tmp.time = new Date().getTime();
	// tmp.isMe = isMine;
	// mChatRecordData.add(lastPosition=mChatRecordData.size(), tmp);
	//// mChatRecordAdapter.notifyDataSetChanged();
	// mChatRecordAdapter.notifyDataSetInvalidated();
	// mChatRecordAdapter.notifyDataSetChanged();
	// setListViewHeightBasedOnChildren(mChatRecordListView,
	// isMaximized?LIST_MAX_PART_CNT:LIST_MIN_PART_CNT);
	// }

	public void loading() {
		ifLoading = true;
		AddRecord(false, ".");
		Timer loadTimer = new Timer();
		loadTimer.schedule(new TimerTask() {
//		Thread th = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
//				while(true) {
				if(ifLoading) {
					String loadText = mChatRecordData.get(mChatRecordData.size() - 1).msg + ".";
					if (loadText.length() > 6) {
						loadText = ".";
					}
					final String text = loadText;
					mChatRecordListView.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(ifLoading) {
								mChatRecordData.get(mChatRecordData.size() - 1).msg = text;
								mChatRecordAdapter.notifyDataSetChanged();
							}
						}
					});
//					try {
//						Thread.sleep(600);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
				}
			}
		}, 0, 600);
		mChatRecordListView.setTag(loadTimer);
//		mChatRecordListView.setTag(th);
	}
	
	@SuppressWarnings("deprecation")
	public void stopLoading() {
//		Thread th = (Thread) mChatRecordListView.getTag();
//		if (th != null) {
////			th.stop();
//			th.destroy();
//			mChatRecordData.remove(mChatRecordData.size() - 1);
//			mChatRecordListView.setTag(null);
//		}
		Timer ttm = (Timer) mChatRecordListView.getTag();
		if (ttm != null) {
			ttm.cancel();
//			ttm.purge();
			ifLoading = false;
			mChatRecordData.remove(mChatRecordData.size() - 1);
			mChatRecordListView.setTag(null);
		}
		// mChatRecordAdapter.notifyDataSetChanged();
		// loadingView = null;
	}

	public void SetSpeak() {
		mChatActivity.Speak();
	}

	public void Update(int index, ListView listview) {
		// 得到第一个可见item项的位置
		int visiblePosition = listview.getFirstVisiblePosition();
		// 得到指定位置的视图，对listview的缓存机制不清楚的可以去了解下
		View view = listview.getChildAt(index - visiblePosition);
		ChatRecordAdapter.ViewHolder vh = (ChatRecordAdapter.ViewHolder) view.getTag();
		((GradientDrawable) vh.tv_msg.getBackground()).setColor(GlobalSettings.THEME_COLOR);
	}

	@Override
	public void ChangeThemeColor() {
		// TODO Auto-generated method stub
		mChatRecordAdapter.notifyDataSetChanged();
	}

	public void CleanRecord()
	{
		mChatRecordData.clear();
		mChatRecordAdapter.notifyDataSetChanged();
	}
}

package com.neo.mytalker.fragments;

import com.neo.mytalker.R;
import com.neo.mytalker.activity.ChatActivity;
import com.neo.mytalker.entity.GlobalSettings;
import com.neo.mytalker.myinterface.ThemeInterface;
import com.neo.mytalker.util.ChatWithTalker;
import com.neo.mytalker.util.Voice2Text;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class ChatBarFragment extends Fragment implements ThemeInterface {

	private View mRoot, mChatActivityView;
	private Context mContext;
	private TextView mSend;
	private EditText mText;
	private ImageView mMore, mVoice;
	private ChatActivity mChatActivity;
	private ChatRecordFragment mChatRecFrag;
	private ChatMenuFragment mChatMenuFragment;
	private LayoutInflater mLayoutInflater;
	private ViewGroup mChatActivityViewGroup;

	public ChatBarFragment(ChatActivity activity) {
		mChatActivity = activity;
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

	private void InitBarBtns() {
		mVoice = (ImageView) mRoot.findViewById(R.id.chat_bottombar_voice);
		mVoice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Voice2Text(mChatActivity, (EditText) mRoot.findViewById(R.id.chat_bottombar_sendingtext))
						.voice2Text();
			}
		});

		mMore = (ImageView) mRoot.findViewById(R.id.chat_bottombar_morefun);
		mMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ((mChatActivity.getWindow()
						.getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)) {

					mChatActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
				}
				mChatMenuFragment.ToggleMenu(mMore);

			}

		});

		mChatMenuFragment = mChatActivity.mChatMenuFragment;

		mText = (EditText) mRoot.findViewById(R.id.chat_bottombar_sendingtext);
		mText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				SendText();
				return false;
			}

		});

		mText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mChatActivity.mChatMenuFragment.isVisible()) {
					mChatActivity.mChatMenuFragment.ToggleMenu(false);
				}
			}
		});

		mText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					mChatMenuFragment.ToggleMenu(false);
					// Log.i(this, "键盘弹起", Toast.LENGTH_SHORT).show();
				}

			}

		});
		mChatRecFrag = mChatActivity.mChatRecFrag;
		mSend = (TextView) mRoot.findViewById(R.id.chat_bottombar_send);
		mSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// get string to send to TR
				SendText();
			}

		});

		mSend.setLongClickable(true);
		mSend.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				new Voice2Text(mChatActivity, (EditText) mRoot.findViewById(R.id.chat_bottombar_sendingtext))
						.voice2Text();
				;
				// Dialog dl = new Dialog(mChatActivity);
				// dl.setTitle("VOICE");
				// dl.show();
				return true;
			}
		});
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return mRoot;
	}

	public void SendText() {
		String tmp = mText.getText().toString();
		if (!tmp.equals("")) {
			mChatRecFrag.AddRecord(true, tmp);
			mText.setText("");
			// to receive from TR

			mChatRecFrag.loading();

			// TODO:Modify the result to update
			new ChatWithTalker(mChatRecFrag, mChatActivity, 0, tmp).execute();
		} else {
			Toast.makeText(mChatActivity, "输入不能为空哦QwQ", Toast.LENGTH_SHORT).show();
		}
	}

	public void SetText(String s) {
		mText.setText(s);
		((EditText)mText).setSelection(mText.getText().toString().length());
	}

	@Override
	public void ChangeThemeColor() {
		mRoot.setBackgroundColor(GlobalSettings.THEME_COLOR);
	}

}

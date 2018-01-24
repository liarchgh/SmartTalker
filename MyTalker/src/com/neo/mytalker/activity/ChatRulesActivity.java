package com.neo.mytalker.activity;
import java.util.ArrayList;

import com.neo.mytalker.R;
import com.neo.mytalker.adapter.ChatRulesAdapter;
import com.neo.mytalker.entity.ChatDialogEntity;
import com.neo.mytalker.myinterface.CustomDialog;
import com.neo.mytalker.myinterface.CustomDialog.Builder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class ChatRulesActivity extends Activity {
	ListView mListView;
	ChatDialogEntity mChatDialogEntity;
	ArrayList<ChatDialogEntity> mChatDialogEntityList;
	ChatRulesAdapter mChatRulesAdapter;
	ImageView mAddRulesBtn;
	CustomDialog.Builder mBuilder;
	CustomDialog mCustomDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat_rules);
		mBuilder = new Builder(this);
		
	}
	
	public void initTestMessage() {
		mChatDialogEntity = new ChatDialogEntity("Are you stupid asshole？", "obviously， you are", 1);
		mChatDialogEntityList.add(mChatDialogEntity);
	}
	
	public void init() {
		mChatDialogEntityList = new ArrayList<ChatDialogEntity>();
		mListView = (ListView) this.findViewById(R.id.rules_list);
		mChatRulesAdapter = new ChatRulesAdapter(mChatDialogEntityList, this);
		mAddRulesBtn = (ImageView) this.findViewById(R.id.add_rules_btn);
		
	}
	
	public void setAdapter() {
		mListView.setAdapter(mChatRulesAdapter);
	}
	
	public void setOnClick() {
		mListView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				ChatDialogEntity item = mChatDialogEntityList.get(position);
				String question = item.getQuestion();
				String answer = item.getAnswer();
				
			}
		});
	}
	
	
}
